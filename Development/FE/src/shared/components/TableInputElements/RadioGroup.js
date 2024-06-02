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

  const { id, onInput, initialValue, validators, teamId } = props;
  const { value, isValid } = inputState;

  useEffect(() => {
    onInput(id, value, isValid, teamId);
  }, [id, value, isValid, onInput, teamId]);

  useEffect(() => {
    // affector //
    if (
      props.affector &&
      props.affector.type === "setting" &&
      value &&
      !teamId
    ) {
      // affector: { id: "-col6-weight", type: "setting", value: WEIGHT_ID },
      const affectorId = id.split("-")[0] + props.affector.id;
      const element = document.getElementById(affectorId);

      const elementLength = element.length;
      for (let i = 1; i < elementLength; i++) {
        element.remove(1);
      }

      Object.keys(props.affector.value[value]).forEach((eventName) => {
        let selectOption = document.createElement("option");
        selectOption.text = eventName;
        selectOption.value = eventName;
        element.add(selectOption);
      });
    }
    // ----------- //
    if (
      props.affector &&
      props.affector.type === "setting" &&
      value &&
      teamId
    ) {
      const idSplit = id.split("-");
      const affectorId = idSplit[0] + "-" + idSplit[1] + props.affector.id;
      const element = document.getElementById(affectorId);

      const elementLength = element.length;
      for (let i = 1; i < elementLength; i++) {
        element.remove(1);
      }

      Object.keys(props.affector.value[value]).forEach((eventName) => {
        let selectOption = document.createElement("option");
        selectOption.text = eventName;
        selectOption.value = eventName;
        element.add(selectOption);
      });
    }
  }, [value, id, props.affector, teamId]);

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
            readOnly={props.readonly}
            disabled={props.disabled}
          />
          {props.showLabel && item}
        </label>
      ))}
    </div>
  );
};

export default RadioGroup;
