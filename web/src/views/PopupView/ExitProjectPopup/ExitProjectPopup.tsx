import React from 'react'
import './ExitProjectPopup.scss'
import { GenericYesNoPopup } from "../GenericYesNoPopup/GenericYesNoPopup";
import {
    updateActiveImageIndex,
    updateActiveLabelNameId,
    updateFirstLabelCreatedFlag,
    updateImageData,
    updateLabelNames
} from "../../../store/labels/actionCreators";
import { AppState } from "../../../store";
import { connect } from "react-redux";
import { ImageData, LabelName } from "../../../store/labels/types";
import { PopupActions } from "../../../logic/actions/PopupActions";
import { ProjectData } from "../../../store/general/types";
import { updateProjectData } from "../../../store/general/actionCreators";

interface IProps {
    updateActiveImageIndex: (activeImageIndex: number) => any;
    updateActiveLabelNameId: (activeLabelId: string) => any;
    updateLabelNames: (labelNames: LabelName[]) => any;
    updateImageData: (imageData: ImageData[]) => any;
    updateFirstLabelCreatedFlag: (firstLabelCreatedFlag: boolean) => any;
    updateProjectData: (projectData: ProjectData) => any;
}

const ExitProjectPopup: React.FC<IProps> = (props) => {
    const {
        updateActiveLabelNameId,
        updateLabelNames,
        updateActiveImageIndex,
        updateImageData,
        updateFirstLabelCreatedFlag,
        updateProjectData
    } = props;

    const renderContent = () => {
        return (
            <div className="ExitProjectPopupContent">
                <div className="Message">
                    确定要退出当前项目吗？ <br />退出前请先确认所有工作已经保存！！！
                </div>
            </div>
        )
    };

    const onAccept = () => {
        updateActiveLabelNameId(null);
        updateLabelNames([]);
        updateProjectData({ projectId: '', type: null, status: '', detail: {}, name: "my-project-name", ticketType: null });
        updateActiveImageIndex(null);
        updateImageData([]);
        updateFirstLabelCreatedFlag(false);
        PopupActions.close();
    };

    const onReject = () => {
        PopupActions.close();
    };

    return (
        <GenericYesNoPopup
            title={"退出项目"}
            renderContent={renderContent}
            acceptLabel={"退出"}
            onAccept={onAccept}
            rejectLabel={"返回"}
            onReject={onReject}
        />)
};

const mapDispatchToProps = {
    updateActiveLabelNameId,
    updateLabelNames,
    updateProjectData,
    updateActiveImageIndex,
    updateImageData,
    updateFirstLabelCreatedFlag
};

const mapStateToProps = (state: AppState) => ({});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(ExitProjectPopup);