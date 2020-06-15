import React from 'react'
import './LoadMoreImagesPopup.scss'
import { AppState } from "../../../store";
import { connect } from "react-redux";
import { addImageData, updateActiveImageIndex } from "../../../store/labels/actionCreators";
import { updateProjectData, updateActivePopupType } from "../../../store/general/actionCreators";
import { GenericYesNoPopup } from "../GenericYesNoPopup/GenericYesNoPopup";

import { ImageData } from "../../../store/labels/types";
import { PopupActions } from "../../../logic/actions/PopupActions";

import { Upload, message } from 'antd';
import { InboxOutlined } from '@ant-design/icons';
import { UploadFile, StartConvert, GetConvertProgress, GetProjectDetail } from '../../../api/api';
import { Progress } from 'antd';
import { ProjectData } from '../../../store/general/types';
import { FileUtil } from '../../../utils/FileUtil';
import { PopupWindowType } from '../../../data/enums/PopupWindowType';
import { ImageActions } from '../../../logic/actions/ImageActions';
import { ViewUtil } from '../../../utils/ViewUtil';

const { Dragger } = Upload;

interface IProps {

    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateActiveImageIndex: (activeImageIndex: number) => any;
    updateProjectData: (projectData: ProjectData) => any;
    addImageData: (imageData: ImageData[]) => any;
    projectData: ProjectData;
}
interface IState {
    fileList: any,
    uploading: boolean
}
class LoadMoreImagesPopup extends React.Component<IProps, IState> {

    getConvertProgressInterval: NodeJS.Timeout;

    state = {
        fileList: [],
        uploading: false,
        converting: false,
        convertPercent: 66,

    };
    onAccept = () => {
        if (this.state.fileList.length > 0) {
            this.handleUpload();
        }
    };


    onReject = () => {
        PopupActions.close();
    };

    render() {
        const props = {
            accept: 'image/*,.pdf',
            multiple: true,
            onRemove: file => {
                this.setState(state => {
                    const index = state.fileList.indexOf(file);
                    const newFileList = state.fileList.slice();
                    newFileList.splice(index, 1);
                    return {
                        fileList: newFileList,
                    };
                });
            },
            beforeUpload: file => {
                this.setState(state => ({
                    fileList: [...state.fileList, file],
                }));
                return false;
            },
            fileList: this.state.fileList
        };
        const renderContent = () => {
            return (<div className="LoadMoreImagesPopupContent">
                <div >
                    <Dragger {...props}>
                        <p className="ant-upload-drag-icon">
                            <InboxOutlined />
                        </p>
                        <p className="ant-upload-text">点击图标,选择需要上传的文件</p>

                    </Dragger>
                </div>

                <div className={this.state.converting ? 'ConvertProgerssContent' : 'HideConvertProgerssContent'}  >

                    正在转换PDF文件
                    <Progress percent={this.state.convertPercent} status="active" showInfo={false} />
                    <p className="convertPercentDisplay">{this.state.convertPercent}%</p>
                </div>

            </div>);
        };

        return (
            <GenericYesNoPopup
                title={"上传文件"}
                renderContent={renderContent}
                acceptLabel={"上传"}
                disableAcceptButton={this.state.fileList.length < 1 || this.state.converting}
                onAccept={this.onAccept}
                rejectLabel={"取消"}
                onReject={this.onReject}
            />
        );

    }


    handleUpload = () => {

        let fileIndex = 0;

        this.doUpload(fileIndex);

    };

    doUpload = (fileIndex) => {
        console.log("fileIndex=", fileIndex);

        const { fileList } = this.state;

        if (fileIndex >= fileList.length) {
            // addImageData(acceptedFiles.map((fileData: File) => FileUtil.mapFileDataToImageData(fileData)));
            // PopupActions.close();
            // this.setState({
            //     fileList: [],
            //     uploading: false,
            // });
            message.success("上传完成");

            let param = {
                id: this.props.projectData.projectId
            };
            StartConvert(param).then((res: any) => {
                console.log('StartConvert', res);
                this.setState(state => ({
                    ...state,
                    converting: true,
                    convertPercent: 0
                }));

                let self = this;
                self.getConvertProgressInterval = setInterval(function () {
                    let queryParam = {
                        id: self.props.projectData.projectId
                    };
                    GetConvertProgress(queryParam).then((res: any) => {
                        console.log('GetConvertProgress', res);
                        var percent = res.data.data.completePercent * 100;

                        self.setState(state => ({
                            ...state,
                            converting: true,
                            convertPercent: percent
                        }));

                        if (percent === 100) {
                            if (self.getConvertProgressInterval) {
                                clearInterval(self.getConvertProgressInterval);
                            }

                            //load project detail
                            GetProjectDetail(queryParam).then((res: any) => {
                                console.log('GetProjectDetail', res.data);
                                let detailArray: [] = res.data.data.detail;
                                self.props.updateProjectData({
                                    ...self.props.projectData,
                                    detail: detailArray
                                });

                                let imgageData = self.getImageData(detailArray);
                                self.props.addImageData(imgageData);
                                self.props.updateActiveImageIndex(-1);
                                self.props.updateActivePopupType(null);
                                PopupActions.close();


                                const index = 0;
                                ViewUtil.fireClickEventOnImagePreview(index, "ImagePreview", 1000);
                                ViewUtil.fireClickEventOnVerticalEditorButton("VerticalEditorButton4RightLabel", 1000);
                            })

                        }
                    });
                }, 5000);
            });


            return;
        }

        this.setState({
            uploading: true,
        });

        let file = fileList[fileIndex];

        const formData = new FormData();
        formData.append('file', file);
        formData.append('id', this.props.projectData.projectId);
        UploadFile(formData).then((res: any) => {
            console.log(res);
            console.log('upload successfully. fileIndex=', fileIndex);
            fileIndex++;
            this.doUpload(fileIndex);

        }, (err: any) => {
            this.setState({
                uploading: false,
            });
            console.log('upload failed. fileIndex=', fileIndex);
        });
    };

    getImageData = (detailArray) => {

        let imgData = [];
        for (let index = 0; index < detailArray.length; index++) {
            const d = detailArray[index];
            let images = d.imageUrls;
            if (images && images.length > 0) {
                for (let imageIndex = 0; imageIndex < images.length; imageIndex++) {
                    const image = images[imageIndex];
                    imgData.push(FileUtil.mapUrlToImageData(image));
                }
            }
        }
        return imgData;
    }
};

const mapDispatchToProps = {
    addImageData,
    updateProjectData,
    updateActivePopupType,
    updateActiveImageIndex,
};

const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData,
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(LoadMoreImagesPopup);