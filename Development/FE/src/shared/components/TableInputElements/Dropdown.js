import React, { useReducer, useEffect } from "react";

// import { validate } from "../../util/validators";
import "./Dropdown.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      return {
        ...state,
        values: action.val,
        // isValid: validate(action.val, action.validators),
      };
    default:
      return state;
  }
};

const Dropdown = (props) => {
  const [inputState, dispatch] = useReducer(inputReducer, {
    values: props.initialValue || [],
    isValid: props.initialValid || false,
  });

  const { id, onInput } = props;
  const { values, isValid } = inputState;

  useEffect(() => {
    onInput(id, values, isValid);
  }, [id, values, isValid, onInput]);

  const changeHandler = (event) => {
    dispatch({
      type: "CHANGE",
      val: event.target.value,
      validators: props.validators,
    });
  };

  return (
    <select
      id={props.id}
      name={props.id}
      className={`table-dropdown `}
      onChange={changeHandler}
      disabled={props.disabled}
    >
      {props.items.map((item, i) => (
        <option key={item + i} value={item}>
          {item}
        </option>
      ))}
    </select>
  );
};

export default Dropdown;
