import React from "react";

import Button from "../../../shared/components/TableInputElements/Button";

import inputField from "./TableInputField";

import { deepEqual } from "../../../shared/util/deepEqual";

function areEqual(prevProps, nextProps) {
  // 같으면 true 다르면 false
  return (
    deepEqual(prevProps.rowData, nextProps.rowData) &&
    prevProps.isEditable === nextProps.isEditable &&
    prevProps.hideText === nextProps.hideText
  );
}

const RegistTableRow = React.memo(
  ({
    version,
    teamId,
    rowData,
    rowIdx,
    showNumber,

    columns,
    modifyColumns,
    checkColumns,

    isEditable,
    hideText,

    inputHandler,
    buttonHandler,
    modifyHandler,
    deleteHandler,
    switchRowHandler,
  }) => {
    if (version === "check") {
      return (
        <tr>
          {showNumber && <td>{rowIdx + 1}</td>}
          {rowData.editable
            ? modifyColumns.map((col, j) => (
                <td key={"row" + rowIdx + "col" + j}>
                  {inputField(
                    col,
                    rowData[col.id],
                    rowIdx,
                    j,
                    j,
                    inputHandler,
                    buttonHandler,
                    teamId,
                    hideText
                  )}
                </td>
              ))
            : columns.map((col, j) => (
                <td key={"row" + rowIdx + "col" + j}>
                  {inputField(
                    col,
                    rowData[col.id],
                    rowIdx,
                    j,
                    j,
                    inputHandler,
                    buttonHandler,
                    teamId,
                    hideText
                  )}
                </td>
              ))}
          {isEditable &&
            (rowData.editable ? (
              <React.Fragment>
                <td>
                  <div className="td-flex">
                    <Button
                      id={teamId + "row" + rowIdx + "-btn-modifyRow"}
                      type="button"
                      className="btn-modify"
                      onClick={modifyHandler}
                    >
                      수정완료
                    </Button>
                    <Button
                      id={teamId + "row" + rowIdx + "-btn-delete"}
                      type="button"
                      className="btn-delete"
                      onClick={deleteHandler}
                    >
                      삭제
                    </Button>
                  </div>
                </td>
              </React.Fragment>
            ) : (
              <td>
                <Button
                  id={teamId + "row" + rowIdx + "-btn-switchRow"}
                  type="button"
                  onClick={switchRowHandler}
                >
                  수정
                </Button>
              </td>
            ))}
        </tr>
      );
    } else if (version === "regist") {
      return (
        <tr>
          {showNumber && <td>{rowIdx + 1}</td>}
          {rowData.isNew
            ? columns.map((col, j) => (
                <td key={"row" + rowIdx + "col" + j}>
                  {inputField(
                    col,
                    rowData[col.id],
                    rowIdx,
                    j,
                    j,
                    inputHandler,
                    buttonHandler,
                    teamId,
                    hideText
                  )}
                </td>
              ))
            : checkColumns.map((col, j) => (
                <td key={"row" + rowIdx + "col" + j}>
                  {inputField(
                    col,
                    rowData[col.id],
                    rowIdx,
                    j,
                    j,
                    inputHandler,
                    buttonHandler,
                    teamId,
                    hideText
                  )}
                </td>
              ))}
          <td>
            {rowData.isNew && (
              <Button
                id={teamId + "row" + rowIdx + "-btn-deleteRow"}
                type="button"
                onClick={deleteHandler}
              >
                삭제
              </Button>
            )}
          </td>
        </tr>
      );
    }
  },
  areEqual
);

export default RegistTableRow;
