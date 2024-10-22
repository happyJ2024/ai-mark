import React, { useState } from 'react';
import { connect } from "react-redux";
import { Direction } from "../../../data/enums/Direction";
import { ISize } from "../../../interfaces/ISize";
import { Settings } from "../../../settings/Settings";
import { AppState } from "../../../store";
import { ImageData } from "../../../store/labels/types";
import ImagesList from "../SideNavigationBar/ImagesList/ImagesList";
import LabelsToolkit from "../SideNavigationBar/LabelsToolkit/LabelsToolkit";
import { SideNavigationBar } from "../SideNavigationBar/SideNavigationBar";
import { VerticalEditorButton } from "../VerticalEditorButton/VerticalEditorButton";
import './EditorContainer.scss';
import Editor from "../Editor/Editor";
import { ContextManager } from "../../../logic/context/ContextManager";
import { ContextType } from "../../../data/enums/ContextType";
import EditorBottomNavigationBar from "../EditorBottomNavigationBar/EditorBottomNavigationBar";
import EditorTopNavigationBar from "../EditorTopNavigationBar/EditorTopNavigationBar";
import { LabelType } from "../../../data/enums/LabelType";

interface IProps {
    windowSize: ISize;
    activeImageIndex: number;
    imagesData: ImageData[];
    activeContext: ContextType;
    activeLabelType: LabelType;
}

const EditorContainer: React.FC<IProps> = ({ windowSize, activeImageIndex, imagesData, activeContext }) => {
    const [leftTabStatus, setLeftTabStatus] = useState(true);
    const [rightTabStatus, setRightTabStatus] = useState(false);

    const calculateEditorSize = (): ISize => {
        if (windowSize) {
            const leftTabWidth = leftTabStatus ? Settings.SIDE_NAVIGATION_BAR_WIDTH_OPEN_PX_LEFT : Settings.SIDE_NAVIGATION_BAR_WIDTH_CLOSED_PX;
            const rightTabWidth = rightTabStatus ? Settings.SIDE_NAVIGATION_BAR_WIDTH_OPEN_PX_RIGHT : Settings.SIDE_NAVIGATION_BAR_WIDTH_CLOSED_PX;
            return {
                width: windowSize.width - leftTabWidth - rightTabWidth,
                height: windowSize.height - Settings.TOP_NAVIGATION_BAR_HEIGHT_PX
                    - Settings.EDITOR_BOTTOM_NAVIGATION_BAR_HEIGHT_PX - Settings.EDITOR_TOP_NAVIGATION_BAR_HEIGHT_PX,
            }
        }
        else
            return null;
    };

    const leftSideBarButtonOnClick = () => {
        if (!leftTabStatus)
            ContextManager.switchCtx(ContextType.LEFT_NAVBAR);
        else if (leftTabStatus && activeContext === ContextType.LEFT_NAVBAR)
            ContextManager.restoreCtx();

        setLeftTabStatus(!leftTabStatus);
    };

    const leftSideBarCompanionRender = () => {
        return <>
            <VerticalEditorButton
                label="图片"
                image={"ico/files.png"}
                imageAlt={"images"}
                onClick={leftSideBarButtonOnClick}
                isActive={leftTabStatus}
            />
        </>
    };

    const leftSideBarRender = () => {
        return <ImagesList />
    };

    const rightSideBarButtonOnClick = () => {
        if (!rightTabStatus)
            ContextManager.switchCtx(ContextType.RIGHT_NAVBAR);
        else if (rightTabStatus && activeContext === ContextType.RIGHT_NAVBAR)
            ContextManager.restoreCtx();

        setRightTabStatus(!rightTabStatus);
    };

    const rightSideBarCompanionRender = () => {
        return <>
            <VerticalEditorButton
                label="标注"
                image={"ico/tags.png"}
                imageAlt={"labels"}
                onClick={rightSideBarButtonOnClick}
                isActive={rightTabStatus}
                extClassName="VerticalEditorButton4RightLabel"
            />
        </>
    };

    const rightSideBarRender = () => {
        return <LabelsToolkit />
    };
    const enableNavigationBar = () => {
        if (imagesData !== undefined && imagesData !== null && imagesData.length > 0 && activeImageIndex >= 0) {
            return true;
        }
        return false;
    };

    return (
        <div className="EditorContainer">
            <SideNavigationBar
                direction={Direction.LEFT}
                isOpen={leftTabStatus}
                isWithContext={activeContext === ContextType.LEFT_NAVBAR}
                renderCompanion={leftSideBarCompanionRender}
                renderContent={leftSideBarRender}
            />
            <div className="EditorWrapper"
                onMouseDown={() => ContextManager.switchCtx(ContextType.EDITOR)}
            >
                <EditorTopNavigationBar enableNavigationBar={enableNavigationBar()} />
                <Editor
                    size={calculateEditorSize()}
                    imageData={imagesData[activeImageIndex]}
                />
                <EditorBottomNavigationBar
                    imageData={imagesData[activeImageIndex]}
                    size={calculateEditorSize()}
                    totalImageCount={imagesData.length}
                />
            </div>
            <SideNavigationBar
                direction={Direction.RIGHT}
                isOpen={rightTabStatus}
                isWithContext={activeContext === ContextType.RIGHT_NAVBAR}
                renderCompanion={rightSideBarCompanionRender}
                renderContent={rightSideBarRender}
            />
        </div>
    );
};

const mapStateToProps = (state: AppState) => ({
    windowSize: state.general.windowSize,
    activeImageIndex: state.labels.activeImageIndex,
    imagesData: state.labels.imagesData,
    activeContext: state.general.activeContext,
    activeLabelType: state.labels.activeLabelType
});

export default connect(
    mapStateToProps
)(EditorContainer);