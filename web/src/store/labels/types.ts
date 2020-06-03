import { IRect } from "../../interfaces/IRect";
import { Action } from "../Actions";
import { LabelType } from "../../data/enums/LabelType";
import { IPoint } from "../../interfaces/IPoint";
import { LabelStatus } from "../../data/enums/LabelStatus";

export type LabelRect = {
    // GENERAL
    id: string;
    labelId: string;
    rect: IRect;// 坐标点

    value?: string;

    // AI
    isCreatedByAI: boolean;
    status: LabelStatus;
    suggestedLabel: string;

    //extend
    labelValue: string;  //文字内容 
}

export type LabelPoint = {
    // GENERAL
    id: string;
    labelId: string;
    point: IPoint;

    // AI
    isCreatedByAI: boolean;
    status: LabelStatus;
    suggestedLabel: string;


}

export type LabelPolygon = {
    id: string;
    labelId: string;
    vertices: IPoint[];
}

export type LabelName = {
    name: string;  // 标签名称
    id: string;    //标签ID

}

export type ImageData = {
    id: string;
    fileData: File;
    loadStatus: boolean;
    labelRects: LabelRect[];
    labelPoints: LabelPoint[];
    labelPolygons: LabelPolygon[];

    imgUrl: string;

    // SSD
    isVisitedByObjectDetector: boolean;

    // POSE NET
    isVisitedByPoseDetector?: boolean;
}

export type LabelsState = {
    activeImageIndex: number;
    activeLabelNameId: string;
    activeLabelType: LabelType;
    activeLabelId: string;
    highlightedLabelId: string;
    imagesData: ImageData[];
    firstLabelCreatedFlag: boolean;
    labels: LabelName[];
}

interface UpdateActiveImageIndex {
    type: typeof Action.UPDATE_ACTIVE_IMAGE_INDEX;
    payload: {
        activeImageIndex: number;
    }
}

interface UpdateActiveLabelNameId {
    type: typeof Action.UPDATE_ACTIVE_LABEL_NAME_ID;
    payload: {
        activeLabelNameId: string;
    }
}

interface UpdateActiveLabelId {
    type: typeof Action.UPDATE_ACTIVE_LABEL_ID;
    payload: {
        activeLabelId: string;
    }
}

interface UpdateHighlightedLabelId {
    type: typeof Action.UPDATE_HIGHLIGHTED_LABEL_ID;
    payload: {
        highlightedLabelId: string;
    }
}

interface UpdateActiveLabelType {
    type: typeof Action.UPDATE_ACTIVE_LABEL_TYPE;
    payload: {
        activeLabelType: LabelType;
    }
}

interface UpdateImageDataById {
    type: typeof Action.UPDATE_IMAGE_DATA_BY_ID;
    payload: {
        id: string;
        newImageData: ImageData;
    }
}

interface AddImageData {
    type: typeof Action.ADD_IMAGES_DATA;
    payload: {
        imageData: ImageData[];
    }
}

interface UpdateImageData {
    type: typeof Action.UPDATE_IMAGES_DATA;
    payload: {
        imageData: ImageData[];
    }
}

interface UpdateLabelNames {
    type: typeof Action.UPDATE_LABEL_NAMES;
    payload: {
        labels: LabelName[];
    }
}

interface UpdateFirstLabelCreatedFlag {
    type: typeof Action.UPDATE_FIRST_LABEL_CREATED_FLAG;
    payload: {
        firstLabelCreatedFlag: boolean;
    }
}

export type LabelsActionTypes = UpdateActiveImageIndex
    | UpdateActiveLabelNameId
    | UpdateActiveLabelType
    | UpdateImageDataById
    | AddImageData
    | UpdateImageData
    | UpdateLabelNames
    | UpdateActiveLabelId
    | UpdateHighlightedLabelId
    | UpdateFirstLabelCreatedFlag

