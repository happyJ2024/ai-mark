import React from 'react';
import './TextInput.scss';

interface IProps {
    inputKey: string;
    label?: string;
    isPassword: boolean;
    onChange?: (event: React.ChangeEvent<HTMLInputElement>) => any;
    onFocus?: (event: React.FocusEvent<HTMLInputElement>) => any;
    inputStyle?: React.CSSProperties;
    labelStyle?: React.CSSProperties;
    barStyle?: React.CSSProperties;
    value?: string;
}

const TextInput = (props: IProps) => {

    const {
        inputKey,
        label,
        isPassword,
        onChange,
        onFocus,
        inputStyle,
        labelStyle,
        barStyle,
        value
    } = props;

    const getInputType = () => {
        return isPassword ? "password" : "text";
    };

    return (
        <div className="TextInput">
            <input
                value={!!value ? value : undefined}
                type={getInputType()}
                id={inputKey}
                style={inputStyle}
                onChange={onChange ? onChange : undefined}
                onFocus={onFocus ? onFocus : undefined}
            />
            {!!label && <label
                htmlFor={inputKey}
                style={labelStyle}
            >
                {label}
            </label>}
            <div
                className="Bar"
                style={barStyle}
            />
        </div>
    );
};

export default TextInput;