import React from 'react';
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

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateProjectData: (projectData: ProjectData) => any;
    projectData: ProjectData;
    imagesData:ImageData[];
}

const TopNavigationBar: React.FC<IProps> = ({ updateActivePopupType, updateProjectData, projectData,imagesData }) => {

    function callOCRSplit() {

    }
    function callOCRExtract() {

    }
     function ifHasFiles() {
        return imagesData && imagesData.length>0;
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
                        {ifHasFiles() &&    <TextButton
                            label={"OCR分类"}
                            externalClassName={"OCRButton"}
                            onClick={() => { callOCRSplit(); }}
                             
                        />}
                         {ifHasFiles() && 
                        <TextButton
                            label={"OCR识别"}
                            externalClassName={"OCRButton"}
                            onClick={() => { callOCRExtract(); }}                             
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