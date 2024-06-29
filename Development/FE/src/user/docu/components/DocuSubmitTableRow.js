import React from "react";

import File from "../../../shared/components/TableInputElements/File";

import { deepEqual } from "../../../shared/util/deepEqual";

function areEqual(prevProps, nextProps) {
  // 같으면 true 다르면 false
  console.log("DOCUTABLE_ROW");
  console.log(prevProps);
  console.log(nextProps);
  return (
    // prevProps.isEditable === nextProps.isEditable &&
    prevProps.hideText === nextProps.hideText &&
    deepEqual(prevProps.rowData, nextProps.rowData)
  );
}

const DocuSubmitTableRow = React.memo(
  ({
    showNumber,
    flatColumns,
    rowData,
    rowIdx,
    type,
    hideText,
    onFileSubmit,
    onFilesSubmit,
  }) => {
    const inputField = (colInfo, initVal, rowidx, colidx, key) => {
      switch (colInfo.type) {
        case "text":
          console.log(initVal);
          return (
            <div
              id={
                "row" + rowidx + "-col" + colidx + "-" + colInfo.id + "-" + type
              }
              key={key}
            >
              &nbsp;{initVal}&nbsp;
            </div>
          );

        case "text-hidden":
          return (
            <div
              id={
                "row" + rowidx + "-col" + colidx + "-" + colInfo.id + "-" + type
              }
              key={key}
            >
              &nbsp;
              {initVal && hideText
                ? initVal.substr(0, colInfo.detail.showCharNum) +
                  "*".repeat(initVal.length - colInfo.detail.showCharNum)
                : initVal}
              &nbsp;
            </div>
          );
        case "submit-button":
          return (
            <div
              id={
                "row" +
                rowidx +
                "-col" +
                colidx +
                "-" +
                colInfo.id +
                "-" +
                type +
                "-div"
              }
              key={key}
            >
              {initVal && (
                <React.Fragment>
                  <div
                    className={`file-status ${
                      initVal.status === "미제출" && "file-emphasize"
                    }`}
                  >
                    {initVal.status}
                  </div>
                  <File
                    id={
                      "row" +
                      rowidx +
                      "-col" +
                      colidx +
                      "-" +
                      colInfo.id +
                      "-" +
                      type
                    }
                    content={
                      initVal.status === "미제출" ? "파일 선택" : "파일 수정"
                    }
                    accept="image/*, .pdf"
                    fileName={initVal.text}
                    onChange={onFileSubmit}
                  />
                </React.Fragment>
              )}
            </div>
          );
        case "submit-button-multiple":
          return (
            <div
              id={
                "row" +
                rowidx +
                "-col" +
                colidx +
                "-" +
                colInfo.id +
                "-" +
                type +
                "-div"
              }
              key={key}
            >
              {initVal && (
                <React.Fragment>
                  <div
                    className={`file-status ${
                      initVal.status === "미제출" && "file-emphasize"
                    }`}
                  >
                    {initVal.status}
                  </div>
                  <File
                    id={
                      "row" +
                      rowidx +
                      "-col" +
                      colidx +
                      "-" +
                      colInfo.id +
                      "-" +
                      type
                    }
                    content="파일 선택"
                    accept="image/*, .pdf"
                    fileName={initVal.text}
                    multiple={true}
                    onChange={onFilesSubmit}
                  />
                </React.Fragment>
              )}
            </div>
          );
        default:
      }
    };

    return (
      <tr>
        {showNumber && <td>{rowIdx + 1}</td>}
        {flatColumns.map((col, j) => (
          <td key={"row" + rowIdx + "col" + j}>
            {inputField(col, rowData[col.id], rowIdx, j)}
          </td>
        ))}
      </tr>
    );
  },
  areEqual
);

export default DocuSubmitTableRow;
