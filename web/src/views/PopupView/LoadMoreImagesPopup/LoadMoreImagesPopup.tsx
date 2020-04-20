import React from 'react'
import './LoadMoreImagesPopup.scss'
import { AppState } from "../../../store";
import { connect } from "react-redux";
import { addImageData } from "../../../store/labels/actionCreators";
import { GenericYesNoPopup } from "../GenericYesNoPopup/GenericYesNoPopup";
import { useDropzone } from "react-dropzone";
import { FileUtil } from "../../../utils/FileUtil";
import { ImageData } from "../../../store/labels/types";
import { AcceptedFileType } from "../../../data/enums/AcceptedFileType";
import { PopupActions } from "../../../logic/actions/PopupActions";

import { Upload, message } from 'antd';
import { InboxOutlined } from '@ant-design/icons';
import { UploadFile } from '../../../api/api';

const { Dragger } = Upload;

interface IProps {
    addImageData: (imageData: ImageData[]) => any;
}
interface IState {
    fileList: any,
    uploading: boolean
}
class LoadMoreImagesPopup extends React.Component<IProps, IState> {

    state = {
        fileList: [],
        uploading: false,
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
            </div>);
        };

        return (
            <GenericYesNoPopup
                title={"上传文件"}
                renderContent={renderContent}
                acceptLabel={"上传"}
                disableAcceptButton={this.state.fileList.length < 1}
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
            return;
        }

        this.setState({
            uploading: true,
        });

        let file = fileList[fileIndex];

        const formData = new FormData();
        formData.append('files', file);

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

};

const mapDispatchToProps = {
    addImageData
};

const mapStateToProps = (state: AppState) => ({});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(LoadMoreImagesPopup);