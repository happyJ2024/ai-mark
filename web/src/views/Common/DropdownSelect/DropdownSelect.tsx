import React from 'react';
import './DropdownSelect.scss';
import { Select } from 'antd';
const { Option } = Select;
interface IProps {
    inputKey: string;
    label?: string;
    options: string[];
    onChange?: (value: string) => any;
    defaultValue?: string 
    labelStyle?: React.CSSProperties;
}

const DropdownSelect = (props: IProps) => {

    const {
        inputKey,
        label,
        options,
        onChange,
        defaultValue,
        labelStyle
    } = props;



    return (
        <div className="DropdownSelect">
            {!!label && <label
                htmlFor={inputKey}
                style={labelStyle}
            >
                {label}
            </label>}
            <Select className="ant-select-jsx"
                id={inputKey}
                defaultValue={defaultValue}
                style={labelStyle}
                onChange={onChange ? onChange : undefined}>
                {!!options && options.map((op) =>
                    <Option key={op} value={op}>{op}</Option>
                )}
            </Select>



        </div>
    );
};

export default DropdownSelect;