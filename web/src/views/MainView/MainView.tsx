import React, {useState} from 'react';
import './MainView.scss';
import {TextButton} from "../Common/TextButton/TextButton";
import classNames from 'classnames';
import {ISize} from "../../interfaces/ISize";
import {ImageButton} from "../Common/ImageButton/ImageButton";
import {ISocialMedia, SocialMediaData} from "../../data/info/SocialMediaData";
import {EditorFeatureData, IEditorFeature} from "../../data/info/EditorFeatureData";
import {Tooltip} from "@material-ui/core";
import Fade from "@material-ui/core/Fade";
import withStyles from "@material-ui/core/styles/withStyles";
import ImagesDropZone from "./ImagesDropZone/ImagesDropZone";
import {PopupWindowType} from "../../data/enums/PopupWindowType";
import {updateProjectData,updateActivePopupType} from "./../../store/general/actionCreators"; 
import {ProjectType} from "../../data/enums/ProjectType";
import {AppState} from "../../store";
import {connect} from "react-redux";
import {ProjectData} from "../../store/general/types";
import {ImageData} from "../../store/labels/types";
import { addImageData, updateActiveImageIndex} from "../../store/labels/actionCreators";
import uuidv1 from 'uuid/v1';
interface IProps {
    updateActiveImageIndex: (activeImageIndex: number) => any;     
    updateProjectData: (projectData: ProjectData) => any;
    addImageData:(imageData: ImageData[])=>any;
    projectData: ProjectData;
}

const MainView: React.FC<IProps> = ({updateActiveImageIndex,   updateProjectData,addImageData,projectData}) => {
    const [projectInProgress, setProjectInProgress] = useState(false);
    const [projectCanceled, setProjectCanceled] = useState(false);

    const startProject = () => {
        //setProjectInProgress(true);
        console.log('startProject');
        
        let projectType=ProjectType.OBJECT_DETECTION;
        console.log('projectType=',projectType);
        console.log('projectData=',projectData);
        console.log('updateProjectData=',updateProjectData);
        updateProjectData({
            ...projectData,
            type: projectType
        });
        
        updateActiveImageIndex(0); 
        var  emptyImageData=[];  
        emptyImageData.push({
            id: uuidv1(),
            fileData: null,
            loadStatus: false,
            labelRects: [],
            labelPoints: [],
            labelPolygons: [],
            isVisitedByObjectDetector: false,
            isVisitedByPoseDetector: false
        });      
        addImageData(emptyImageData);
       console.log('addImageData')
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

    const DarkTooltip = withStyles(theme => ({
        tooltip: {
            backgroundColor: "#171717",
            color: "#ffffff",
            boxShadow: theme.shadows[1],
            fontSize: 11,
            maxWidth: 120
        },
    }))(Tooltip);

    const getSocialMediaButtons = (size:ISize) => {
        return SocialMediaData.map((data:ISocialMedia, index: number) => {
            return <DarkTooltip
                key={index}
                disableFocusListener
                title={data.tooltipMessage}
                TransitionComponent={Fade}
                TransitionProps={{ timeout: 600 }}
                placement="left"
            >
                <div>
                    <ImageButton
                        buttonSize={size}
                        image={data.imageSrc}
                        imageAlt={data.imageAlt}
                        href={data.href}
                    />
                </div>
            </DarkTooltip>
        });
    };

    const getEditorFeatureTiles = () => {
        return EditorFeatureData.map((data:IEditorFeature) => {
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
                    <div className="TriangleVerticalContent"/>
                </div>
            </div>

            <div className="Slider" id="upper">
                <div className="TriangleVertical">
                    <div className="TriangleVerticalContent"/>
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
                    <div className="TriangleVerticalContent"/>
                </div>
                {projectInProgress && <TextButton
                    label={"Go Back"}
                    onClick={endProject}
                />}
            </div>
            <div className="RightColumn">
                <div/>
                <ImagesDropZone/>
                <div className="SocialMediaWrapper">
                    {/* {getSocialMediaButtons({width: 30, height: 30})} */}
                    {!projectInProgress && <TextButton
                    label={"开始标注"}
                    onClick={startProject}
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
    updateActiveImageIndex, 
    updateProjectData, 
    addImageData
};
const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData
});
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(MainView);