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
    case "SET":
      return {
        ...state,
        values: action.val,
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

  const { id, onInput, initialValue, validators } = props;
  const { values, isValid } = inputState;

  useEffect(() => {
    onInput(id, values, isValid);
  }, [id, values, isValid, onInput]);

  useEffect(() => {
    // affector //
    if (props.affector && props.affector.type === "disabled") {
      // affector: { id: "-col3-nationality", type: "disabled", value: "외국인" },
      const affectorId = id.split("-")[0] + props.affector.id;
      if (values.includes(props.affector.value)) {
        document.getElementById(affectorId).disabled = false;
      } else {
        document.getElementById(affectorId).disabled = true;
      }
    }
    // ----------- //
  }, [values, id, props.affector]);

  useEffect(() => {
    dispatch({
      type: "SET",
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
