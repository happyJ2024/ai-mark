import React from 'react';
import { ISize } from "../../../../interfaces/ISize";
import Scrollbars from 'react-custom-scrollbars';
import { ImageData, LabelName, LabelRect } from "../../../../store/labels/types";
import './RectLabelsList.scss';
import {
    updateActiveLabelId,
    updateActiveLabelNameId,
    updateImageDataById
} from "../../../../store/labels/actionCreators";
import { AppState } from "../../../../store";
import { connect } from "react-redux";
import LabelInputField from "../LabelInputField/LabelInputField";
import EmptyLabelList from "../EmptyLabelList/EmptyLabelList";
import { LabelActions } from "../../../../logic/actions/LabelActions";
import { LabelStatus } from "../../../../data/enums/LabelStatus";
import { findLast } from "lodash";
import { LabelPreDefine } from '../../../../settings/LabelPreDefine';

interface IProps {
    size: ISize;
    imageData: ImageData;
    updateImageDataById: (id: string, newImageData: ImageData) => any;
    activeLabelId: string;
    highlightedLabelId: string;
    updateActiveLabelNameId: (activeLabelId: string) => any;
    labelNames: LabelName[];
    updateActiveLabelId: (activeLabelId: string) => any;
}

const RectLabelsList: React.FC<IProps> = ({ size, imageData, updateImageDataById, labelNames, updateActiveLabelNameId, activeLabelId, highlightedLabelId, updateActiveLabelId }) => {
    const labelInputFieldHeight = 60;
    const labelInputFieldHeight_Text = 40;
    const listStyle: React.CSSProperties = {
        width: size.width,
        height: size.height
    };
    const listStyleContent: React.CSSProperties = {
        width: size.width,
        height: imageData === undefined ? 0 : imageData.labelRects.length * labelInputFieldHeight
    };

    const deleteRectLabelById = (labelRectId: string) => {
        LabelActions.deleteRectLabelById(imageData.id, labelRectId);
    };

    const updateRectLabel = (labelRectId: string, labelNameId: string) => {
        const newImageData = {
            ...imageData,
            labelRects: imageData.labelRects
                .map((labelRect: LabelRect) => {
                    if (labelRect.id === labelRectId) {
                        return {
                            ...labelRect,
                            labelId: labelNameId,
                            status: LabelStatus.ACCEPTED
                        }
                    } else {
                        return labelRect
                    }
                })
        };
        updateImageDataById(imageData.id, newImageData);
        updateActiveLabelNameId(labelNameId);
    };
    const updateRectLabelValue = (labelRectId: string, labelValue: string) => {
        const newImageData = {
            ...imageData,
            labelRects: imageData.labelRects
                .map((labelRect: LabelRect) => {
                    if (labelRect.id === labelRectId) {
                        return {
                            ...labelRect,
                            labelValue: labelValue,
                            status: LabelStatus.ACCEPTED
                        }
                    } else {
                        return labelRect
                    }
                })
        };
        updateImageDataById(imageData.id, newImageData);

    };

    const onClickHandler = () => {
        updateActiveLabelId(null);
    };
    //Label显示列表
    const getChildren = () => {


        let waybillLabelRect = [];
        let invoiceLabelRect = [];
        let invoiceItemsLabelRect = [];
        let unknownLabelRect = [];
        imageData.labelRects.forEach(element => {
            const findLabelName = element.labelId !== null ? findLast(labelNames, { id: element.labelId }) : null;
            if (findLabelName != null) {
                if (findLabelName.name.indexOf(LabelPreDefine.WAYBILL_KEYWORDS_PREFIX) >= 0) {
                    waybillLabelRect.push(element);
                }
                else if (findLabelName.name.indexOf(LabelPreDefine.INVOICE_KEYWORDS_PREFIX) >= 0) {

                    if (findLabelName.name.indexOf(LabelPreDefine.INVOICE_ITEMS_KEYWORDS_PREFIX) == -1) {
                        invoiceLabelRect.push(element);
                    }
                    if (findLabelName.name.indexOf(LabelPreDefine.INVOICE_ITEMS_KEYWORDS_PREFIX) >= 0) {
                        let findExist = false;
                        invoiceItemsLabelRect.forEach(t => {
                            if (t.labelGroupId === element.labelGroupId) {
                                t.labelRects.push(element);
                                findExist = true;
                                return;
                            }
                        })
                        if (findExist == false) {
                            invoiceItemsLabelRect.push({
                                labelGroupId: element.labelGroupId,
                                labelRects: [element]
                            });
                        }
                    }
                }

            } else {
                unknownLabelRect.push(element);
            }
        });
        console.log("waybillLabelRect:", waybillLabelRect);
        console.log("invoiceLabelRect:", invoiceLabelRect);
        console.log("invoiceItemsLabelRect:", invoiceItemsLabelRect);
        console.log("unknownLabelRect:", unknownLabelRect);


        var waybillList = () => {
            if (waybillLabelRect.length > 0) {
                return <div>
                    <p className="CategoryText">{LabelPreDefine.WAYBILL_KEYWORDS_PREFIX}</p>
                    {waybillLabelRect
                        .map((labelRect: LabelRect) => {
                            return <LabelInputField
                                size={{
                                    width: size.width,
                                    height: labelInputFieldHeight
                                }}
                                isActive={labelRect.id === activeLabelId}
                                isHighlighted={labelRect.id === highlightedLabelId}
                                id={labelRect.id}
                                key={labelRect.id}
                                labelValue={labelRect.labelValue}
                                labelRectPoint={labelRect.rect}
                                onDelete={deleteRectLabelById}
                                value={labelRect.labelId !== null ? findLast(labelNames, { id: labelRect.labelId }) : null}
                                options={labelNames}
                                onSelectLabel={updateRectLabel}
                                onUpdateLabelValue={updateRectLabelValue}
                            />
                        })}
                </div>;
            }
            return null;
        }
        var invoiceList = () => {
            if (invoiceLabelRect.length > 0 || invoiceItemsLabelRect.length > 0) {
                return <div>
                    <p className="CategoryText">{LabelPreDefine.INVOICE_KEYWORDS_PREFIX}</p>
                    {invoiceLabelRect
                        .map((labelRect: LabelRect) => {
                            return <LabelInputField
                                size={{
                                    width: size.width,
                                    height: labelInputFieldHeight
                                }}
                                isActive={labelRect.id === activeLabelId}
                                isHighlighted={labelRect.id === highlightedLabelId}
                                id={labelRect.id}
                                key={labelRect.id}
                                labelValue={labelRect.labelValue}
                                labelRectPoint={labelRect.rect}
                                onDelete={deleteRectLabelById}
                                value={labelRect.labelId !== null ? findLast(labelNames, { id: labelRect.labelId }) : null}
                                options={labelNames}
                                onSelectLabel={updateRectLabel}
                                onUpdateLabelValue={updateRectLabelValue}
                            />
                        })}
                    {
                        invoiceItemsLabelRect.map(t => {
                        return <div><p className="CategotyTextItems">{LabelPreDefine.INVOICE_ITEMS_KEYWORDS_PREFIX}-{t.labelGroupId}</p>
                                {
                                    t.labelRects.map((itemRect: LabelRect) => {
                                        return <LabelInputField
                                            size={{
                                                width: size.width,
                                                height: labelInputFieldHeight
                                            }}
                                            isActive={itemRect.id === activeLabelId}
                                            isHighlighted={itemRect.id === highlightedLabelId}
                                            id={itemRect.id}
                                            key={itemRect.id}
                                            labelValue={itemRect.labelValue}
                                            labelRectPoint={itemRect.rect}
                                            onDelete={deleteRectLabelById}
                                            value={itemRect.labelId !== null ? findLast(labelNames, { id: itemRect.labelId }) : null}
                                            options={labelNames}
                                            onSelectLabel={updateRectLabel}
                                            onUpdateLabelValue={updateRectLabelValue}
                                        />

                                    })
                                }
                            </div>
                        })
                    }
                </div>;
            }
            return null;
        }

        var unknownList = () => {
            if (unknownLabelRect.length > 0) {
                return <div>
                    <p className="CategoryText">未定义</p>
                    {unknownLabelRect
                        .map((labelRect: LabelRect) => {
                            return <LabelInputField
                                size={{
                                    width: size.width,
                                    height: labelInputFieldHeight
                                }}
                                isActive={labelRect.id === activeLabelId}
                                isHighlighted={labelRect.id === highlightedLabelId}
                                id={labelRect.id}
                                key={labelRect.id}
                                labelValue={labelRect.labelValue}
                                labelRectPoint={labelRect.rect}
                                onDelete={deleteRectLabelById}
                                value={labelRect.labelId !== null ? findLast(labelNames, { id: labelRect.labelId }) : null}
                                options={labelNames}
                                onSelectLabel={updateRectLabel}
                                onUpdateLabelValue={updateRectLabelValue}
                            />
                        })}
                </div>;
            }
            return null;
        }

        return <div>
            {waybillList()}
            {invoiceList()}
            {unknownList()}
        </div>
    };
    const getLabelCount = () => {

        if (imageData !== undefined && imageData !== null && imageData.labelRects !== undefined && imageData.labelRects !== null) {
            return imageData.labelRects.filter((labelRect: LabelRect) => labelRect.status === LabelStatus.ACCEPTED).length;
        }
        return 0;

    };

    return (
        <div
            className="RectLabelsList"
            style={listStyle}
            onClickCapture={onClickHandler}
        >
            {getLabelCount() === 0 ?
                <EmptyLabelList
                    labelBefore={"还没有任何标注信息"}
                    labelAfter={"当前图片还没有任何标注信息"}
                /> :
                <Scrollbars>
                    <div
                        className="RectLabelsListContent"
                        style={listStyleContent}
                    >
                        {getChildren()}
                    </div>
                </Scrollbars>
            }
        </div>
    );
};

const mapDispatchToProps = {
    updateImageDataById,
    updateActiveLabelNameId,
    updateActiveLabelId
};

const mapStateToProps = (state: AppState) => ({
    activeLabelId: state.labels.activeLabelId,
    highlightedLabelId: state.labels.highlightedLabelId,
    labelNames: state.labels.labels
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(RectLabelsList);