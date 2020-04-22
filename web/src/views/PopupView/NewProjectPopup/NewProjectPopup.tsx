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
import { CreateProject, GetProjectList, DeleteProject, GetProjectDetail } from '../../../api/api';
import { Tabs, Table, Tag, message } from 'antd';

const { Column, ColumnGroup } = Table;
const { TabPane } = Tabs;

interface IProps {
    updateActivePopupType: (activePopupType: PopupWindowType) => any;
    updateLabelNames: (labels: LabelName[]) => any;
    updateActiveImageIndex: (activeImageIndex: number) => any;
    updateProjectData: (projectData: ProjectData) => any;
    projectData: ProjectData;

}
interface IState {
    newProjectData: ProjectData,
    projectList: any;
    loading: boolean;
    currentTabKey: string
}
class NewProjectPopup extends React.Component<IProps, IState>  {



    state = {
        newProjectData: this.props.projectData,
        projectList: [],
        loading: false,
        currentTabKey: '',
    };

    onChangeName = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log("onChangeName");
        const value = event.target.value;

        this.setState({
            ...this.state,
            newProjectData: {
                ...this.state.newProjectData,
                name: value
            }

        })
    };
    onChangeTicketType = (value: string) => {
        console.log("onChangeTicketType");

        this.setState({
            ...this.state,
            newProjectData: {
                ...this.state.newProjectData,
                ticketType: TicketType[value]
            }

        })
    };
    onCreateAccept = () => {

        console.log('new projectData=', this.state.newProjectData);

        const param = {
            projectId: this.state.newProjectData.projectId,
            name: this.state.newProjectData.name
        };
        CreateProject(param).then((res: any) => {
            console.log(res);
            if (!res.data) return;

            let createResult = res.data.data;

            let updateData = {
                ...this.state.newProjectData,
                projectId: createResult.id + "",
                status: createResult.status
            };
            console.log(updateData);

            this.props.updateProjectData(updateData);
            this.props.updateActiveImageIndex(-1);
            // this.initDefaultLabels();
            this.props.updateActivePopupType(null);
        })
    };
    initDefaultLabels = () => {
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
        this.props.updateLabelNames(newLabelNames);
        console.log("initDefaultLabels", newLabelNames);
    };


    onCreateReject = () => {
        this.props.updateActivePopupType(null);
    };

    AllSupportTicketTypes = (): string[] => {
        return [
            TicketType.DHL_AIR.toString(),
            TicketType.DHL_SEA.toString()
        ];

    };
    callback = (key): void => {
        console.log(key);
        this.setState({
            ...this.state,
            currentTabKey: key
        })
        if (key === "projectList") {
            //load project list

            let param = {};
            GetProjectList(param).then((res: any) => {
                console.log(res);
                if (!res.data) return;

                let queryResult = res.data.data;

                this.setState({
                    ...this.state,
                    projectList: queryResult.map(d => {
                        return {
                            projectId: d.id,
                            name: d.name,
                            status: d.status,
                            tags: d.status === "undone" ? ['继续', '删除'] : ['删除']
                        }
                    })

                });
            });

        }
    }
    columns = [
        {
            title: '名称',
            key: 'name',
            dataIndex: 'name',
            width: '50%',
        },
        {
            title: '状态', key: 'status',
            dataIndex: 'status',
            sorter: true,
            width: '20%',
            render: (d: string) => {
                let display = d === 'done' ? '完成' : '未完成';
                return <span>{display}</span>
            }
        },
        {
            title: '操作', key: 'tags',
            dataIndex: 'tags',
            width: '30%',
            render: (tags, record) => (
                <span>
                    {tags.map(tag => {
                        let color = 'geekblue';
                        if (tag === 'delete') {
                            color = 'red';
                        }
                        return (
                            <Tag className="ProjectOperationTag" color={color} key={tag} onClick={this.onTagClick.bind(this, record, tag)}>
                                {tag}
                            </Tag>
                        );
                    })}
                </span>
            ),
        },
    ];
    onTagClick = (record, tag, e) => {
        e.preventDefault();
        console.log(record, tag);

        if (tag === "删除") {

            DeleteProject({ id: record.projectId }).then((res: any) => {
                console.log(res);
                if (res.data && res.data.errorCode === 0) {
                    message.success("删除成功");
                }
                this.callback("projectList");
            })
        }
        else if (tag === "继续") {

            let updateData = {
                ...this.state.newProjectData,
                ...record
            };
            console.log(updateData);
            this.props.updateProjectData(updateData);
            //load project detail
            let queryParam = {
                id: record.projectId
            };
            GetProjectDetail(queryParam).then((res: any) => {
                console.log('GetProjectDetail', res);

                if (res.data && res.data.data) {
                    this.props.updateProjectData({
                        ...this.props.projectData,
                        detail: res.data.data.detail
                    });
                    this.props.updateActiveImageIndex(-1);
                    this.props.updateActivePopupType(null);
                }

            })

        }
    }

    renderContent = () => {
        return (
            <Tabs defaultActiveKey="1" onChange={this.callback}>
                <TabPane tab="创建项目" key="newProject">
                    <div className="NewProjectPopup">

                        <div className="RightContainer">
                            <div className="Message">
                                你可以在这里设置项目的基本属性，包括单据类型和属性设置.
                            </div>
                            <div className="LabelsContainer">
                                {<Scrollbars>
                                    <div className="NewProjectContent"            >
                                        <div className="LabelEntry" key="projectName">
                                            <TextInput
                                                inputKey={"projectName"}
                                                value={this.state.newProjectData.name}
                                                isPassword={false}
                                                label={"名称"}
                                                onChange={this.onChangeName}

                                            />
                                        </div>
                                        {/* <div className="LabelEntry" key="projectTicktetType">
                    <DropdownSelect
                        inputKey={"projectTicktetType"}
                        options={AllSupportTicketTypes()}
                        label={"票据类型"}
                        onChange={onChangeTicketType}
                        labelStyle={{ width: 300 }} />
                </div> */}
                                    </div>
                                </Scrollbars>}
                            </div>
                        </div>
                    </div>
                </TabPane>
                <TabPane tab="历史项目" key="projectList">
                    <div className="NewProjectPopup">

                        <div className="RightContainer">
                            <div className="Message">
                                你可以在这里查看历史项目, 并打开未完成的项目.
    </div>
                            <div className="ProjectTableContainer">
                                <Table
                                    columns={this.columns}
                                    rowKey={record => record.projectId}
                                    dataSource={this.state.projectList}
                                    loading={this.state.loading}
                                    pagination={{ pageSize: 5 }}
                                >
                                </Table>
                            </div>
                        </div>
                    </div>
                </TabPane>

            </Tabs>


        );
    };

    render() {
        return <GenericYesNoPopup
            title={"项目设置"}
            renderContent={this.renderContent}
            acceptLabel={"创建"}
            onAccept={this.onCreateAccept}
            skipAcceptButton={this.state.currentTabKey === 'projectList'}
            rejectLabel={"取消"}
            onReject={this.onCreateReject}
        />
    }
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
