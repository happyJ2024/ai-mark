import React, { useState } from 'react';
import './TopNavigationBar.scss';
import StateBar from "../StateBar/StateBar";
import { UnderlineTextButton } from "../../Common/UnderlineTextButton/UnderlineTextButton";
import { TextButton } from "../../Common/TextButton/TextButton";
import { PopupWindowType } from "../../../data/enums/PopupWindowType";
import { AppState } from "../../../store";
import { connect } from "react-redux";
import { updateActivePopupType, updateProjectData } from "../../../store/general/actionCreators";
// import { ImageButton } from "../../Common/ImageButton/ImageButton";
// import { Settings } from "../../../settings/Settings";
import { ProjectData } from "../../../store/general/types";
import { ImageData } from "../../../store/labels/types";
import { CallOCR, Publish } from '../../../api/api';
import { message, Modal, Spin } from 'antd';

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateProjectData: (projectData: ProjectData) => any;
    projectData: ProjectData;
    imagesData: ImageData[];
}

const TopNavigationBar: React.FC<IProps> = ({ updateActivePopupType, updateProjectData, projectData, imagesData }) => {

    const [ocrSuccess, setocrSuccess] = useState(false);

    const [ocrWorking, setOcrWorking] = useState(false);
    const [publishing, setPublishing] = useState(false)

    function callPublish() {
        setPublishing(true);
        let param = {
            id: projectData.projectId
        };

        Publish(param).then((res: any) => {
            if (res.data && res.data.errorCode === 0) {
                message.success("推送结果成功");
            } else {
                message.error("推送结果失败");
            }
        }).finally(() => {
            setPublishing(false);
        })

    }
    function callOCRExtract() {

        setOcrWorking(true);

        let param = {
            id: projectData.projectId
        };

        CallOCR(param).then((res: any) => {
            if (res.data && res.data.errorCode === 0) {
                message.success("OCR提取成功");
                setocrSuccess(true);
            } else {
                message.error("OCR提取失败");
            }
        }).finally(() => {
            setOcrWorking(false);
        })

    }
    function ifHasFiles() {
        return imagesData && imagesData.length > 0;
    }

    return (
        <div className="TopNavigationBar">
            <StateBar />
            <div className="TopNavigationBarWrapper">
                <div>
                    <div
                        className="Header"
                    >
                        <img
                            draggable={false}
                            alt={"make-sense"}
                            src={"/make-sense-ico-transparent.png"}
                        />
                        AI Mark
                        <TextButton
                            label={"上传文件"}
                            externalClassName={"UploadImageButton"}
                            onClick={() => updateActivePopupType(PopupWindowType.LOAD_IMAGES)}
                        />

                        {ifHasFiles() &&
                            <TextButton
                                label={"OCR识别"}
                                externalClassName={"OCRButton"}
                                onClick={() => { callOCRExtract(); }}
                            />}

                        {ifHasFiles() && ocrSuccess && <TextButton
                            label={"推送结果"}
                            externalClassName={"OCRButton"}
                            onClick={() => { callPublish(); }}

                        />}

                    </div>

                </div>

                <div className="NavigationBarGroupWrapper">
                    <div className="ProjectName">项目:{projectData.name}</div>
                    {/* <TextInput
                        inputKey={"ProjectName"}
                        isPassword={false}
                        value={projectData.name}
                        onChange={onChange}
                        onFocus={onFocus}
                    /> */}
                </div>
                <div className="NavigationBarGroupWrapper">

                    {/* <TextButton
                        label={"导出标注"}
                        externalClassName={"ExportLabelButton"}
                        onClick={() => updateActivePopupType(PopupWindowType.EXPORT_LABELS)}
                    />
                    <UnderlineTextButton
                        label={"设置"}
                        under={true}
                        onClick={() => updateActivePopupType(PopupWindowType.UPDATE_LABEL_NAMES)}
                    /> */}

                    <UnderlineTextButton
                        label={"退出"}
                        under={true}
                        onClick={() => updateActivePopupType(PopupWindowType.EXIT_PROJECT)}
                    />
                    {/* <ImageButton
                        image={"img/exit-new.png"}
                        imageAlt={"exit-new.png"}
                        buttonSize={{ width: 35, height: 30 }}
                        // href={Settings.GITHUB_URL}
                        onClick={() => updateActivePopupType(PopupWindowType.EXIT_PROJECT)}
                    /> */}
                </div>
            </div>

            <Modal
                title="OCR提取"
                visible={ocrWorking}
                onOk={null}
                onCancel={null}
                footer={null}
            >
                <div className="OCRModelContent">
                    <Spin size="large" tip="正在提取中..." />
                </div>

            </Modal>
            <Modal
                title="推送结果"
                visible={publishing}
                onOk={null}
                onCancel={null}
                footer={null}
            >
                <div className="OCRModelContent">
                    <Spin size="large" tip="正在推送结果中..." />
                </div>

            </Modal>
        </div>
    );
};

const mapDispatchToProps = {
    updateActivePopupType,
    updateProjectData,
};

const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData,
    imagesData: state.labels.imagesData
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(TopNavigationBar);