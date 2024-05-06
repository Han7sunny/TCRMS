import { useCallback, useReducer } from "react";

const registReducer = (state, action) => {
  switch (action.type) {
    case "INPUT_CHANGE":
      let idArr = action.inputId.split("-");
      let row = Number(idArr[0].replace("row", ""));

      let data = state.inputs;

      if (idArr.length === 3) {
        data[row][idArr[2]] = action.value;
      }
      if (idArr.length === 4) {
        let subidx = Number(idArr[3].replace("input", ""));
        data[row][idArr[2]][subidx] = action.value;
      }

      return {
        ...state,
        inputs: data,
      };

    case "ADD_ROW":
      let addRow = state.inputs;
      addRow.push(action.value);

      return {
        ...state,
        inputs: addRow,
      };

    case "DELETE_ROW":
      let dataDelete = state.inputs;
      dataDelete.splice(action.row, 1);

      return {
        ...state,
        inputs: [...dataDelete],
      };

    case "SET_DATA":
      return {
        inputs: action.inputs,
      };

    default:
      return state;
  }
};

export const useRegist = (initialInputs, defaultInputs) => {
  const [registState, dispatch] = useReducer(registReducer, {
    inputs: initialInputs,
  });

  const inputHandler = useCallback((id, value, isValid) => {
    dispatch({
      type: "INPUT_CHANGE",
      value: value,
      isValid: isValid,
      inputId: id,
    });
  }, []);

  const addRow = useCallback(() => {
    dispatch({
      type: "ADD_ROW",
      value: defaultInputs,
    });
  }, [defaultInputs]);

  const deleteRow = useCallback((row) => {
    dispatch({
      type: "DELETE_ROW",
      row: row,
    });
  }, []);

  const setRegistData = useCallback((inputData) => {
    dispatch({
      type: "SET_DATA",
      inputs: inputData,
    });
  }, []);

  return [registState, inputHandler, addRow, deleteRow, setRegistData];
};
