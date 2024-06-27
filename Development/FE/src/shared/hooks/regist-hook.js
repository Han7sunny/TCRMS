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

    case "INPUT_CHANGE_TEAM":
      let idArrTeam = action.inputId.split("-");
      let teamIdx = Number(idArrTeam[0].replace("team", ""));
      let memberIdx = Number(idArrTeam[1].replace("row", ""));

      let dataTeam = state.inputs;
      if (idArrTeam.length === 4) {
        dataTeam[teamIdx].teamMembers[memberIdx][idArrTeam[3]] = action.value;
      }
      if (idArrTeam.length === 5) {
        let subidx = Number(idArrTeam[4].replace("input", ""));
        dataTeam[teamIdx].teamMembers[memberIdx][idArrTeam[3]][subidx] =
          action.value;
      }

      return {
        ...state,
        inputs: dataTeam,
      };

    case "ADD_ROW":
      let addRow = state.inputs;
      const newRow = JSON.parse(JSON.stringify(action.value));
      addRow.push(newRow);

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

  const inputHandler = useCallback((id, value, isValid, teamId) => {
    if (teamId) {
      dispatch({
        type: "INPUT_CHANGE_TEAM",
        value: value,
        isValid: isValid,
        inputId: id,
      });
    } else {
      dispatch({
        type: "INPUT_CHANGE",
        value: value,
        isValid: isValid,
        inputId: id,
      });
    }
  }, []);

  const addRow = useCallback(
    (input) => {
      dispatch({
        type: "ADD_ROW",
        value: input ? input : defaultInputs,
      });
    },
    [defaultInputs]
  );

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
