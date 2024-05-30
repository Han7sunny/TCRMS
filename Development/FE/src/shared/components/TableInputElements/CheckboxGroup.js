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

  const { id, onInput, initialValue, validators, teamId } = props;
  const { values, isValid } = inputState;

  useEffect(() => {
    onInput(id, values, isValid, teamId);
  }, [id, values, isValid, onInput, teamId]);

  useEffect(() => {
    // affector //
    if (props.affector && props.affector.type === "disabled" && !teamId) {
      // affector: { id: "-col3-nationality", type: "disabled", value: "외국인" },
      props.affector.id.forEach((affectorIdSuffix) => {
        const affectorId = id.split("-")[0] + affectorIdSuffix;
        if (values.includes(props.affector.value)) {
          document.getElementById(affectorId).disabled = false;
        } else {
          document.getElementById(affectorId).disabled = true;
        }
      });
    }
    // ----------- //
    if (props.affector && props.affector.type === "disabled" && teamId) {
      props.affector.id.forEach((affectorIdSuffix) => {
        const idSplit = id.split("-");
        const affectorId = idSplit[0] + "-" + idSplit[1] + affectorIdSuffix;
        if (values.includes(props.affector.value)) {
          document.getElementById(affectorId).disabled = false;
        } else {
          document.getElementById(affectorId).disabled = true;
        }
      });
    }
  }, [values, id, props.affector, teamId]);

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
            readOnly={props.readonly}
            disabled={props.disabled}
          />
          {props.showLabel && item}
        </label>
      ))}
    </div>
  );
};

export default CheckboxGroup;
