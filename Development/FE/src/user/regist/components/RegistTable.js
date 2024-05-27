import React, { useState } from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";
import Tooltip from "@material-ui/core/Tooltip";

import "./RegistTable.css";

const RegistTable = (props) => {
  const teamId = props.teamId || "";

  const inputField = (colInfo, initVal, rowidx, colidx, key) => {
    switch (colInfo.type) {
      case "text":
        if (Array.isArray(initVal)) {
          return (
            <div
              id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
              key={key}
            >
              &nbsp;
              {initVal.join(colInfo.detail ? colInfo.detail.separator : "")}
              &nbsp;
            </div>
          );
        } else {
          return (
            <div
              id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
              key={key}
            >
              &nbsp;{initVal}&nbsp;
            </div>
          );
        }
      case "text-hidden":
        const val = initVal.join("");
        return (
          <div
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
          >
            &nbsp;
            {initVal[0] && hideText
              ? val.substr(0, colInfo.detail.showCharNum) +
                "*".repeat(val.length - colInfo.detail.showCharNum)
              : val}
            &nbsp;
          </div>
        );
      case "input":
        return (
          <Input
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            element="input"
            type="text"
            onInput={props.inputHandler}
            teamId={teamId}
            validators={colInfo.detail.validators}
            placeholder={colInfo.detail.placeholder}
            initialValue={initVal}
          />
        );
      case "radio-group":
        return (
          <RadioGroup
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            initialValue={initVal}
            onInput={props.inputHandler}
            teamId={teamId}
            showLabel={colInfo.detail.showLabel}
            affector={colInfo.detail.affector}
          />
        );
      case "checkbox-group":
        return (
          <CheckboxGroup
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            initialValue={initVal}
            onInput={props.inputHandler}
            teamId={teamId}
            showLabel={colInfo.detail.showLabel}
            affector={colInfo.detail.affector}
          />
        );
      case "dropdown":
        return (
          <Dropdown
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            onInput={props.inputHandler}
            teamId={teamId}
            initialValue={initVal}
          />
        );
      case "button":
        return (
          <Button
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            type="button"
            onClick={props.buttonHandler}
          >
            {colInfo.detail.content}
          </Button>
        );
      case "multi-input":
        return (
          <div
            className="div-flex"
            id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
          >
            {colInfo.detail.map((item, i) =>
              inputField(item, initVal[i], rowidx, colidx, colInfo.id + i)
            )}
          </div>
        );
      default:
    }
  };

  const [hideText, setHideText] = useState(true);

  let bodyElement;
  if (props.version === "check") {
    bodyElement = props.data.map((row, i) => (
      <tr key={i}>
        {props.showNumber && <td>{i + 1}</td>}
        {row.editable
          ? props.modifyColumns.map((col, j) => (
              <td key={"row" + i + "col" + j}>
                {inputField(col, row[col.id], i, j)}
              </td>
            ))
          : props.columns.map((col, j) => (
              <td key={"row" + i + "col" + j}>
                {inputField(col, row[col.id], i, j)}
              </td>
            ))}
        {row.editable ? (
          <React.Fragment>
            <td>
              <div className="td-flex">
                <Button
                  id={teamId + "row" + i + "-btn-modifyRow"}
                  type="button"
                  className="btn-modify"
                  onClick={props.modifyHandler}
                >
                  수정완료
                </Button>
                <Button
                  id={teamId + "row" + i + "-btn-delete"}
                  type="button"
                  className="btn-delete"
                  onClick={props.deleteHandler}
                >
                  삭제
                </Button>
              </div>
            </td>
          </React.Fragment>
        ) : (
          <td>
            <Button
              id={teamId + "row" + i + "-btn-switchRow"}
              type="button"
              onClick={props.switchRowHanlder}
            >
              수정
            </Button>
          </td>
        )}
      </tr>
    ));
  } else if (props.version === "regist") {
    bodyElement = props.data.map((row, i) => (
      <tr key={i}>
        {props.showNumber && <td>{i + 1}</td>}
        {row.isNew
          ? props.columns.map((col, j) => (
              <td key={"row" + i + "col" + j}>
                {inputField(col, row[col.id], i, j)}
              </td>
            ))
          : props.checkColumns.map((col, j) => (
              <td key={"row" + i + "col" + j}>
                {inputField(col, row[col.id], i, j)}
              </td>
            ))}
        <td>
          {row.isNew && (
            <Button
              id={teamId + "row" + i + "-btn-deleteRow"}
              type="button"
              onClick={props.deleteHandler}
            >
              삭제
            </Button>
          )}
        </td>
      </tr>
    ));
  }

  return (
    <table id={props.tableId} className="regist-table">
      <colgroup>
        {props.columns.map((col, i) => (
          <col key={col.id} className={"table-col-" + i} />
        ))}
        {props.showNumber && (
          <col className={"table-col-" + props.columns.length} />
        )}
        <col className="table-col-btn" />
      </colgroup>
      <thead>
        <tr>
          {props.showNumber && <th></th>}
          {props.columns.map((col) => {
            if (col.type === "text-hidden") {
              return (
                <th key={col.id}>
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
            } else if (col.name === "비고") {
              return (
                <th key={col.id}>
                  {col.name}
                  <Tooltip
                    title="외국인 선수이며 외국인등록번호가 없는 경우 개인식별을 위해 폰번호나 이메일 주소 기입"
                    placement="top"
                  >
                    <img
                      src={`${process.env.PUBLIC_URL}/img/info_24dp.png`}
                      width={"14px"}
                    />
                  </Tooltip>{" "}
                </th>
              );
            } else {
              return <th key={col.id}>{col.name}</th>;
            }
          })}
          <th></th>
        </tr>
      </thead>
      <tbody>{bodyElement}</tbody>
    </table>
  );
};

export default RegistTable;
