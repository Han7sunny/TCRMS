import React, { useState } from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import CheckboxBool from "../../../shared/components/TableInputElements/CheckboxBool";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";
import Tooltip from "@material-ui/core/Tooltip";

import "./RegistTable.css";

const RegistTeamTable = (props) => {
  const teamId = props.teamId || "";

  const inputField = (colInfo, initVal, rowidx, colidx, key, disabled) => {
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
        } else if (initVal === "후보 선수") {
          return (
            <div
              id={teamId + "row" + rowidx + "-col" + colidx + "-" + colInfo.id}
              key={key}
            >
              &nbsp;{initVal}&nbsp;
              {props.editMode && (
                <CheckboxBool
                  id={teamId + "row" + rowidx + "-col" + colidx + "-editable"}
                  key={key}
                  item={true}
                  initialValue={!disabled} //!row.editable = disabled
                  onInput={props.inputHandler}
                  teamId={teamId}
                />
              )}
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
        let val = initVal.join("");
        if (val === "-") val = "";
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
            disabled={disabled || colInfo.detail.disabled}
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
            disabled={disabled || colInfo.detail.disabled}
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
            disabled={disabled || colInfo.detail.disabled}
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
            disabled={disabled || colInfo.detail.disabled}
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
              inputField(
                item,
                initVal[i],
                rowidx,
                colidx,
                colInfo.id + i,
                disabled
              )
            )}
          </div>
        );
      default:
    }
  };

  const [hideText, setHideText] = useState(true);

  let bodyElement = props.data.map((row, i) => (
    <tr key={i}>
      {props.showNumber && <td>{i + 1}</td>}
      {props.editMode
        ? props.modifyColumns.map((col, j) => (
            <td key={"row" + i + "col" + j}>
              {inputField(col, row[col.id], i, j, null, !row.editable)}
            </td>
          )) // editable
        : props.columns.map((col, j) => (
            <td key={"row" + i + "col" + j}>
              {inputField(col, row[col.id], i, j, null, false)}
            </td>
          ))}
    </tr>
  ));

  const infoToolTip = (
    <Tooltip
      title={
        <div className="table-comments">
          외국인 선수이며{" "}
          <span className="info-highlight-case">
            외국인등록번호가 없는 경우
          </span>
          <br />
          개인식별을 위해 <span className="info-highlight">폰번호</span>나{" "}
          <span className="info-highlight">이메일 주소</span> 기입
        </div>
      }
      placement="top"
    >
      <img
        src={`${process.env.PUBLIC_URL}/img/info_24dp.png`}
        width={"14px"}
        alt="비고"
      />
    </Tooltip>
  );

  return (
    <table id={props.tableId} className="regist-table">
      <colgroup>
        {props.columns.map((col, i) => (
          <col key={col.id} className={"table-col-" + i} />
        ))}
        {props.showNumber && (
          <col className={"table-col-" + props.columns.length} />
        )}
      </colgroup>
      <thead>
        <tr>
          {props.showNumber && <th></th>}
          {props.editMode
            ? props.modifyColumns.map((col) => {
                if (col.name === "비고") {
                  return (
                    <th key={col.id}>
                      {col.name}
                      {infoToolTip}{" "}
                    </th>
                  );
                } else {
                  return <th key={col.id}>{col.name}</th>;
                }
              })
            : props.columns.map((col) => {
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
                      {infoToolTip}{" "}
                    </th>
                  );
                } else {
                  return <th key={col.id}>{col.name}</th>;
                }
              })}
        </tr>
      </thead>
      <tbody>{bodyElement}</tbody>
    </table>
  );
};

export default RegistTeamTable;
