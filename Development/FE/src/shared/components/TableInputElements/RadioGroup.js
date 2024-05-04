import React, { useReducer, useEffect } from "react";

// import { validate } from "../../util/validators";
import "./RadioGroup.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      return {
        ...state,
        value: action.val,
        // isValid: validate(action.val, action.validators),
      };
    default:
      return state;
  }
};

const RadioGroup = (props) => {
  const [inputState, dispatch] = useReducer(inputReducer, {
    value: props.initialValue || "",
    isValid: props.initialValid || false,
  });

  const { id, onInput } = props;
  const { value, isValid } = inputState;

  useEffect(() => {
    onInput(id, value, isValid);
  }, [id, value, isValid, onInput]);

  const changeHandler = (event) => {
    dispatch({
      type: "CHANGE",
      val: event.target.value,
      validators: props.validators,
    });
  };

  return (
    <div id={props.id} className={`table-radio `}>
      {props.items.map((item, i) => (
        <label key={item + i}>
          <input
            type="radio"
            name={props.id}
            value={item}
            checked={inputState.value === item}
            onChange={changeHandler}
          />
          {props.showLabel && item}
        </label>
      ))}
    </div>
  );
};

export default RadioGroup;
