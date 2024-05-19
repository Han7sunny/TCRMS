import React, { useReducer, useEffect } from "react";

import { validate } from "../../util/validators";
import "./Input.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      return {
        ...state,
        value: action.val,
        isValid: validate(action.val, action.validators),
      };
    case "TOUCH": {
      return {
        ...state,
        isTouched: true,
        isValid: validate(action.val, action.validators),
      };
    }
    default:
      return state;
  }
};

const Input = (props) => {
  const [inputState, dispatch] = useReducer(inputReducer, {
    value: props.initialValue || "",
    isTouched: false,
    isValid: props.initialValid || false,
  });

  const { id, onInput, initialValue, validators, teamId } = props;
  const { value, isValid } = inputState;

  useEffect(() => {
    onInput(id, value, isValid, teamId);
  }, [id, value, isValid, onInput, teamId]);

  useEffect(() => {
    dispatch({
      type: "CHANGE",
      val: initialValue,
      validators: validators,
    });
  }, [initialValue, validators]);

  const changeHandler = (event) => {
    dispatch({
      type: "CHANGE",
      val: event.target.value,
      validators: validators,
    });
  };

  const touchHandler = (event) => {
    dispatch({
      type: "TOUCH",
      val: event.target.value,
      validators: props.validators,
    });
  };

  const element =
    props.element === "input" ? (
      <input
        id={props.id}
        type={props.type}
        placeholder={props.placeholder}
        onChange={changeHandler}
        onBlur={touchHandler}
        value={inputState.value}
        readOnly={props.readonly}
        disabled={props.disabled}
      />
    ) : (
      <textarea
        id={props.id}
        rows={props.rows || 3}
        onChange={changeHandler}
        onBlur={touchHandler}
        value={inputState.value}
        readOnly={props.readonly}
        disabled={props.disabled}
      />
    );

  return (
    <div
      className={`table-input ${
        !inputState.isValid && inputState.isTouched && "table-input--invalid"
      }`}
    >
      {/* <label htmlFor={props.id}>{props.label}</label> */}
      {element}
      {!inputState.isValid && inputState.isTouched && props.errorText && (
        <p>{props.errorText}</p>
      )}
    </div>
  );
};

export default Input;
