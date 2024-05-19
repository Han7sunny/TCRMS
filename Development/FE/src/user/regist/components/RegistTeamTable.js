import React, { useState } from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistTable.css";

{
  /* <RegistTeamTable
    columns={TABLE_COLUMNS_CHECK_TEAM}
    modifyColumns={TABLE_COLUMNS_REGIST_TEAM}
    data={team.teamMembers}
    inputHandler={inputHandler}
    editMode={team.editable}
    teamId={`team${i}-`}
  /> */
}

const RegistTeamTable = (props) => {
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
