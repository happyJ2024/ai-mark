/* eslint-disable jsx-a11y/alt-text */
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

import bgImage from "./../../yuanbao-bg2.png"

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
            name: "project" + (new Date()).getTime(),
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

        return <div className="WorkProgressContent" >
            {/* <img src={"img/workprogress.png"}></img> */}
        </div>;

        // return EditorFeatureData.map((data: IEditorFeature) => {
        //     return <div
        //         className="EditorFeaturesTiles"
        //         key={data.displayText}
        //     >
        //         <div
        //             className="EditorFeaturesTilesWrapper"
        //         >
        //             <img
        //                 draggable={false}
        //                 alt={data.imageAlt}
        //                 src={data.imageSrc}
        //             />
        //             <div className="EditorFeatureLabel">
        //                 {data.displayText}
        //             </div>
        //         </div>
        //     </div>
        // });
    };

    return (
        <div className={getClassName()}  style={{backgroundImage:'url('+bgImage+')', backgroundRepeat:'no-repeat', backgroundSize:'cover'}}>
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
                    <div>
                        <img
                            draggable={false}
                            src={"make-sense-ico-transparent.png"}
                        />
                        <span>&nbsp;AI OCR</span></div>
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
                {/* <ImagesDropZone /> */}
                <div className="SocialMediaWrapper">
                    {/* {getSocialMediaButtons({width: 30, height: 30})} */}
                    {!projectInProgress && <TextButton
                        externalClassName={"StartMarkButton"}
                        label={"开始OCR"}
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