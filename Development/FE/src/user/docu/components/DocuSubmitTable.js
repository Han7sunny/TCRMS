import React, { useState } from "react";

import File from "../../../shared/components/TableInputElements/File";

import "./DocuSubmitTable.css";

const DocuSubmitTable = (props) => {
  const inputField = (colInfo, initVal, rowidx, colidx, key) => {
    switch (colInfo.type) {
      case "text":
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
              props.type
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
              "row" +
              rowidx +
              "-col" +
              colidx +
              "-" +
              colInfo.id +
              "-" +
              props.type
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
              props.type +
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
                    props.type
                  }
                  content={
                    initVal.status === "미제출" ? "파일 선택" : "파일 수정"
                  }
                  accept="image/*, .pdf"
                  fileName={initVal.text}
                  onChange={props.onFileSubmit}
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
              props.type +
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
                    props.type
                  }
                  content="파일 선택"
                  accept="image/*, .pdf"
                  fileName={initVal.text}
                  multiple={true}
                  onChange={props.onFilesSubmit}
                />
              </React.Fragment>
            )}
          </div>
        );
      default:
    }
  };

  const [hideText, setHideText] = useState(true);

  let flatColumns = [];
  props.columns.forEach((col) => {
    if (col.columns) {
      flatColumns.push(...col.columns);
    } else flatColumns.push(col);
  });

  const bodyElement = props.data.map((row, i) => (
    <tr key={i}>
      {props.showNumber && <td>{i + 1}</td>}
      {flatColumns.map((col, j) => (
        <td key={"row" + i + "col" + j}>
          {inputField(col, row[col.id], i, j)}
        </td>
      ))}
    </tr>
  ));

  return (
    // <form action="#" method="POST" encType="multipart/form-data">
    <table className="docu-table">
      <colgroup>
        {flatColumns.map((col, i) => (
          <col key={col.id} className={"table-col-" + i} />
        ))}
        {props.showNumber && (
          <col className={"table-col-" + flatColumns.length} />
        )}
      </colgroup>
      <thead>
        <tr>
          {props.showNumber && <th rowSpan={2}></th>}
          {props.columns.map((col) => {
            if (col.columns) {
              return (
                <th key={col.id} rowSpan={1} colSpan={col.columns.length}>
                  {col.name}
                </th>
              );
            } else {
              if (col.type === "text-hidden") {
                return (
                  <th key={col.id} rowSpan={2}>
                    {col.name}{" "}
                    <button
                      className="table-column__hide-btn"
                      onClick={() => {
                        setHideText(!hideText);
                      }}
                    >
                      {hideText ? "보이기" : "숨기기"}
                    </button>
                  </th>
                );
              } else {
                return (
                  <th key={col.id} rowSpan={2}>
                    {col.name}
                  </th>
                );
              }
            }
          })}
        </tr>
        <tr>
          {props.columns
            .filter((col) => col.columns)
            .map((col) =>
              col.columns.map((col) => (
                <th key={col.id} rowSpan={1}>
                  {col.name}
                </th>
              ))
            )}
        </tr>
      </thead>
      <tbody>{bodyElement}</tbody>
    </table>
    // </form>
  );
};

export default DocuSubmitTable;
