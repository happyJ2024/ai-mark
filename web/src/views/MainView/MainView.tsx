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
            name: "project-" + (dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss")),
            ticketType: null
        });
        updateActivePopupType(PopupWindowType.START_NEW_PROJECT);

    };

    // 对Date的扩展，将 Date 转化为指定格式的String   
    // 月(M)、日(d)、小时(H)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
    // 例子：   
    // (new Date()).Format("yyyy-MM-dd HH:mm:ss.S") ==> 2006-07-02 08:09:04.423   
    // (new Date()).Format("yyyy-M-d H:m:s.S")      ==> 2006-7-2 8:9:4.18   
    const dateFormat = function (date, fmt) { //author: meizz   
        var o = {
            "M+": date.getMonth() + 1,                 //月份   
            "d+": date.getDate(),                    //日   
            "h+": date.getHours(),                   //小时   
            "m+": date.getMinutes(),                 //分   
            "s+": date.getSeconds(),                 //秒   
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度   
            "S": date.getMilliseconds()             //毫秒   
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

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
        <div className={getClassName()} style={{ backgroundImage: 'url(' + bgImage + ')', backgroundRepeat: 'no-repeat', backgroundSize: 'cover' }}>
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