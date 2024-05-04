import React, { useReducer, useEffect } from "react";

// import { validate } from "../../util/validators";
import "./CheckboxGroup.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      let inValues = false;
      let tmpValues = [];

      state.values.forEach((value) => {
        if (value === action.val) inValues = true;
        else tmpValues.push(value);
      });

      if (!inValues) tmpValues.push(action.val);

      return {
        ...state,
        values: tmpValues,
        // isValid: validate(action.val, action.validators),
      };
    default:
      return state;
  }
};

const CheckboxGroup = (props) => {
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
    <div id={props.id} className={`table-checkbox `}>
      {props.items.map((item, i) => (
        <label key={item + i}>
          <input
            type="checkbox"
            name={props.id}
            value={item}
            checked={inputState.values.includes(item)}
            onChange={changeHandler}
          />
          {props.showLabel && item}
        </label>
      ))}
    </div>
  );
};

export default CheckboxGroup;
