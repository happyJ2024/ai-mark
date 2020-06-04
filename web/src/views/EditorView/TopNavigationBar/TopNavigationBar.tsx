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
import { ImageData, LabelName } from "../../../store/labels/types";
import { CallOCR, Publish } from '../../../api/api';
import { message, Modal, Spin } from 'antd';
import { LabelPreDefine } from '../../../settings/LabelPreDefine';
import uuid from 'uuid';
import { LabelStatus } from '../../../data/enums/LabelStatus';
import { findLast } from 'lodash';
import { updateImageDataById, updateActiveImageIndex } from '../../../store/labels/actionCreators';
import { ContextManager } from '../../../logic/context/ContextManager';
import { ContextType } from '../../../data/enums/ContextType';

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateProjectData: (projectData: ProjectData) => any;
    updateActiveImageIndex: (activeImageIndex: number) => any;
    projectData: ProjectData;
    imagesData: ImageData[];
    labelNames: LabelName[];
}

const TopNavigationBar: React.FC<IProps> = ({ updateActivePopupType, updateProjectData, projectData, imagesData, labelNames }) => {
    const [rightTabStatus, setRightTabStatus] = useState(false);
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

    //OCR 调用
    function callOCRExtract() {

        setOcrWorking(true);

        let param = {
            id: projectData.projectId
        };

        CallOCR(param).then((res: any) => {
            if (res.data && res.data.errorCode === 0) {
                message.success("OCR提取成功");
                setocrSuccess(true);

                updateLabelRectByOcrResult(res.data.data);

                refreshImageData();

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

    //刷新图片，获取最新矫正过的图片
    const refreshImageData = () => {
        for (let index = 0; index < imagesData.length; index++) {
            let currentImageData = imagesData[index];
            if (currentImageData) {
                let newImgUrl = currentImageData.imgUrl;
                if (newImgUrl.indexOf('?t=') < 0) {
                    newImgUrl += "?t="
                }
                newImgUrl += (new Date()).getTime();
                currentImageData.loadStatus = false;
                currentImageData.imgUrl = newImgUrl; 
                updateImageDataById(currentImageData.id, currentImageData);
            }
        }
    }
    //根据OCR结果更新标签列表
    function updateLabelRectByOcrResult(ocrResult) {

        for (let index = 0; index < imagesData.length; index++) {
            let currentImageData = imagesData[index];
            if (currentImageData) {
                var list = currentImageData.labelRects;
                list.splice(0, list.length);
                const newImageData = {
                    ...currentImageData,
                    labelRects: list
                };
                updateImageDataById(currentImageData.id, newImageData);
            }
        }
        // return;
        //waybill
        if (ocrResult.waybill && ocrResult.waybill.length > 0) {
            let waybill = ocrResult.waybill[0];
            LabelPreDefine.WAYBILL_KEYWORDS.forEach(key => {
                let pObject = waybill[key];
                if (pObject) {
                    let imageDataIndex = pObject.pageNum - 1;

                    let id = uuid();
                    let labelName = LabelPreDefine.WAYBILL_KEYWORDS_PREFIX + key;
                    let labelId = findMatchLabelId(labelNames, labelName);
                    let rect = getLabelRect(pObject.rect)

                    let status = LabelStatus.ACCEPTED
                    let isCreatedByAI = true;
                    let suggestedLabel = '';

                    let labelValue = pObject.words;
                    let labelGroupId = 0;

                    let currentImageData = imagesData[imageDataIndex];
                    if (currentImageData) {
                        var list = currentImageData.labelRects;
                        list.push({
                            id: id,
                            labelId: labelId,
                            rect: rect,
                            status: status,
                            isCreatedByAI: isCreatedByAI,
                            suggestedLabel: suggestedLabel,
                            labelValue: labelValue,
                            labelGroupId: labelGroupId
                        });

                        const newImageData = {
                            ...currentImageData,
                            labelRects: list
                        };
                        updateImageDataById(currentImageData.id, newImageData);

                    }
                }
            });

        }
        //invoice
        if (ocrResult.invoice && ocrResult.invoice.length > 0) {

            ocrResult.invoice.forEach(invoice => {

                LabelPreDefine.INVOICE_KEYWORDS.forEach(key => {
                    let pObject = invoice[key];
                    if (pObject) {
                        let imageDataIndex = pObject.pageNum - 1;

                        let id = uuid();
                        let labelName = LabelPreDefine.INVOICE_KEYWORDS_PREFIX + key;
                        let labelId = findMatchLabelId(labelNames, labelName);
                        let rect = getLabelRect(pObject.rect)

                        let status = LabelStatus.ACCEPTED
                        let isCreatedByAI = true;
                        let suggestedLabel = '';

                        let labelValue = pObject.words;
                        let labelGroupId = invoice.InvoiceSeq;

                        let currentImageData = imagesData[imageDataIndex];
                        if (currentImageData) {
                            var list = currentImageData.labelRects;
                            list.push({
                                id: id,
                                labelId: labelId,
                                rect: rect,
                                status: status,
                                isCreatedByAI: isCreatedByAI,
                                suggestedLabel: suggestedLabel,
                                labelValue: labelValue,
                                labelGroupId: labelGroupId
                            });

                            const newImageData = {
                                ...currentImageData,
                                labelRects: list
                            };
                            updateImageDataById(currentImageData.id, newImageData);

                        }
                    }
                });

                //Items
                let items = invoice.Items;
                items.forEach(item => {
                    LabelPreDefine.INVOICE_ITEMS_KEYWORDS.forEach(key => {
                        let pObject = item[key];
                        if (pObject) {
                            let imageDataIndex = pObject.pageNum - 1;

                            let id = uuid();
                            let labelName = LabelPreDefine.INVOICE_ITEMS_KEYWORDS_PREFIX + key;
                            let labelId = findMatchLabelId(labelNames, labelName);
                            let rect = getLabelRect(pObject.rect)

                            let status = LabelStatus.ACCEPTED
                            let isCreatedByAI = true;
                            let suggestedLabel = '';

                            let labelValue = pObject.words;
                            let labelGroupId = item.ItemSeq;

                            let currentImageData = imagesData[imageDataIndex];
                            if (currentImageData) {
                                var list = currentImageData.labelRects;
                                list.push({
                                    id: id,
                                    labelId: labelId,
                                    rect: rect,
                                    status: status,
                                    isCreatedByAI: isCreatedByAI,
                                    suggestedLabel: suggestedLabel,
                                    labelValue: labelValue,
                                    labelGroupId: labelGroupId
                                });

                                const newImageData = {
                                    ...currentImageData,
                                    labelRects: list
                                };
                                updateImageDataById(currentImageData.id, newImageData);

                            }
                        }
                    });
                });

            });
        }


    }
    function findMatchLabelId(list: LabelName[], name: string): string {

        var id = '';
        if (list) {
            list.forEach((element) => {

                if (element.name === name) {
                    id = element.id;
                    return;
                }
            });
        }
        return id;
    }
    function getLabelRect(rect: number[]) {
        var result = { x: 0, y: 0, width: 0, height: 0 }
        if (rect && rect.length == 4) {
            result.x = rect[0];
            result.y = rect[1];
            result.width = rect[2] - rect[0];
            result.height = rect[3] - rect[1];
        }
        return result;
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
                            src={"make-sense-ico-transparent.png"}
                        />
                        AI OCR
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
    updateImageDataById,
    updateActiveImageIndex,
};

const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData,
    imagesData: state.labels.imagesData,
    labelNames: state.labels.labels
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(TopNavigationBar);