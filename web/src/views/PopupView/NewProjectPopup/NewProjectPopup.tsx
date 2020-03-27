import React from 'react'
import './NewProjectPopup.scss'
import { GenericYesNoPopup } from "../GenericYesNoPopup/GenericYesNoPopup";
import { PopupWindowType } from "../../../data/enums/PopupWindowType";
import { updateLabelNames } from "../../../store/labels/actionCreators";
import { updateActivePopupType, updateProjectData } from "../../../store/general/actionCreators";
import { AppState } from "../../../store";
import { connect } from "react-redux";
import Scrollbars from 'react-custom-scrollbars';
import TextInput from "../../Common/TextInput/TextInput";
import { LabelName } from "../../../store/labels/types";
import { ProjectData } from '../../../store/general/types';
import { updateActiveImageIndex } from "../../../store/labels/actionCreators";
import { LabelsSelector } from '../../../store/selectors/LabelsSelector';
import uuidv1 from 'uuid/v1';
import { TicketType } from '../../../data/enums/TicketType';
import DropdownSelect from '../../Common/DropdownSelect/DropdownSelect';

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateLabelNames: (labels: LabelName[]) => any;
    updateActiveImageIndex: (activeImageIndex: number) => any;
    updateProjectData: (projectData: ProjectData) => any;
    projectData: ProjectData;

}
const NewProjectPopup: React.FC<IProps> = ({ updateActivePopupType, updateLabelNames, updateActiveImageIndex, updateProjectData, projectData }) => {

    const onChangeName = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log("onChangeName");
        const value = event.target.value;

        updateProjectData({
            ...projectData,
            name: value
        })

    };
    const onChangeTicketType = (value: string) => {
        console.log("onChangeTicketType")
        updateProjectData({
            ...projectData,
            ticketType: TicketType[value]
        })
    };
    const onCreateAccept = () => {

        console.log('projectData=', projectData);

        updateProjectData(projectData);

        updateActiveImageIndex(-1);
        initDefaultLabels();
        updateActivePopupType(null);
    };
    const initDefaultLabels = () => {
        const exitLabelNames = LabelsSelector.getLabelNames();
        if (!!exitLabelNames && exitLabelNames.length > 0) {
            console.log("exitLabelNames：", exitLabelNames);
            return;
        }
        const newLabelNames: LabelName[] = [];
        newLabelNames.push({
            name: "ID",
            id: uuidv1()
        });
        newLabelNames.push({
            name: "NAME",
            id: uuidv1()
        });
        updateLabelNames(newLabelNames);
        console.log("initDefaultLabels", newLabelNames);
    };


    const onCreateReject = () => {
        updateActivePopupType(null);
    };

    const AllSupportTicketTypes = (): string[] => {
        return [
            TicketType.DHL_AIR.toString(),
            TicketType.DHL_SEA.toString()
        ];

    };


    const renderContent = () => {
        return (<div className="NewProjectPopup">

            <div className="RightContainer">
                <div className="Message">
                    你可以在这里设置项目的基本属性，包括单据类型和属性设置.
                </div>
                <div className="LabelsContainer">
                    {<Scrollbars>
                        <div
                            className="NewProjectContent"
                        >
                            <div className="LabelEntry" key="projectName">
                                <TextInput
                                    inputKey={"projectName"}
                                    value={projectData.name}
                                    isPassword={false}
                                    label={"名称"}
                                    onChange={onChangeName}

                                />
                            </div>
                            <div className="LabelEntry" key="projectTicktetType">
                                <DropdownSelect
                                    inputKey={"projectTicktetType"}
                                    options={AllSupportTicketTypes()}
                                    label={"票据类型"}
                                    onChange={onChangeTicketType}
                                    labelStyle={{ width: 300 }} />

                            </div>
                        </div>
                    </Scrollbars>}
                </div>
            </div>
        </div>);
    };

    return (
        <GenericYesNoPopup
            title={"项目设置"}
            renderContent={renderContent}
            acceptLabel={"创建"}
            onAccept={onCreateAccept}
            rejectLabel={"取消"}
            onReject={onCreateReject}
        />)
};

const mapDispatchToProps = {
    updateActivePopupType,
    updateLabelNames,
    updateActiveImageIndex,
    updateProjectData
};

const mapStateToProps = (state: AppState) => ({
    projectData: state.general.projectData
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(NewProjectPopup);
