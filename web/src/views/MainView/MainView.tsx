import React, { useState } from 'react';
import './MainView.scss';
import { TextButton } from "../Common/TextButton/TextButton";
import classNames from 'classnames';
import { EditorFeatureData, IEditorFeature } from "../../data/info/EditorFeatureData";
import ImagesDropZone from "./ImagesDropZone/ImagesDropZone";
import { updateActivePopupType, updateProjectData } from "./../../store/general/actionCreators";
import { connect } from "react-redux";
import { PopupWindowType } from '../../data/enums/PopupWindowType';
import { ProjectType } from '../../data/enums/ProjectType';
import { ProjectData } from '../../store/general/types';
import { AppState } from '../../store';

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any; updateProjectData: (projectData: ProjectData) => any;
    projectData: ProjectData;
}
const MainView: React.FC<IProps> = ({ updateActivePopupType, updateProjectData, projectData }) => {
    const [projectInProgress, setProjectInProgress] = useState(false);
    const [projectCanceled, setProjectCanceled] = useState(false);

    const startNewProject = () => {
        //setProjectInProgress(true);

        console.log('startNewProject');
        updateProjectData({
            ...projectData,
            type: ProjectType.OBJECT_DETECTION,
            name: "project001",
            ticketType: null
        });
        updateActivePopupType(PopupWindowType.START_NEW_PROJECT);

    };


    const endProject = () => {
        setProjectInProgress(false);
        setProjectCanceled(true);
    };

    const getClassName = () => {
        return classNames(
            "MainView", {
            "InProgress": projectInProgress,
            "Canceled": !projectInProgress && projectCanceled
        }
        );
    };



    const getEditorFeatureTiles = () => {
        return EditorFeatureData.map((data: IEditorFeature) => {
            return <div
                className="EditorFeaturesTiles"
                key={data.displayText}
            >
                <div
                    className="EditorFeaturesTilesWrapper"
                >
                    <img
                        draggable={false}
                        alt={data.imageAlt}
                        src={data.imageSrc}
                    />
                    <div className="EditorFeatureLabel">
                        {data.displayText}
                    </div>
                </div>
            </div>
        });
    };

    return (
        <div className={getClassName()}>
            <div className="Slider" id="lower">
                <div className="TriangleVertical">
                    <div className="TriangleVerticalContent" />
                </div>
            </div>

            <div className="Slider" id="upper">
                <div className="TriangleVertical">
                    <div className="TriangleVerticalContent" />
                </div>
            </div>

            <div className="LeftColumn">
                <div className={"LogoWrapper"}>
                    <img
                        draggable={false}
                        alt={"main-logo"}
                        src={"img/main-image-color.png"}
                    />
                </div>
                <div className="EditorFeaturesWrapper">
                    {getEditorFeatureTiles()}
                </div>
                <div className="TriangleVertical">
                    <div className="TriangleVerticalContent" />
                </div>
                {projectInProgress && <TextButton
                    label={"Go Back"}
                    onClick={endProject}
                />}
            </div>
            <div className="RightColumn">
                <div />
                <ImagesDropZone />
                <div className="SocialMediaWrapper">
                    {/* {getSocialMediaButtons({width: 30, height: 30})} */}
                    {!projectInProgress && <TextButton
                        externalClassName={"StartMarkButton"}
                        label={"开始标注"}
                        onClick={startNewProject}
                    />}
                </div>
                {/* {!projectInProgress && <TextButton
                    label={"Get Started"}
                    onClick={startProject}
                />} */}
            </div>
        </div>
    );
};

const mapDispatchToProps = {
    updateActivePopupType,
    updateProjectData

};
const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData
});
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(MainView);