import React, { memo } from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";

const RegistTableBodyElement = (props) => {
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
          console.log(initVal);
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
            {initVal[0] && true
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
            disabled={colInfo.detail.disabled}
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
            disabled={colInfo.detail.disabled}
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
            disabled={colInfo.detail.disabled}
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
            disabled={colInfo.detail.disabled}
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

  if (props.version === "check") {
    // console.log("CHECKRECDER");
    // console.log(props.data);
    // bodyElement = props.data.map((row, i) => (
    return (
      <tr>
        {props.showNumber && <td>{props.i + 1}</td>}
        {props.row.editable
          ? props.modifyColumns.map((col, j) => (
              <td key={"row" + props.i + "col" + j}>
                {inputField(col, props.row[col.id], props.i, j)}
              </td>
            ))
          : props.columns.map((col, j) => (
              <td key={"row" + props.i + "col" + j}>
                {inputField(col, props.row[col.id], props.i, j)}
              </td>
            ))}
        {props.isEditable &&
          (props.row.editable ? (
            <React.Fragment>
              <td>
                <div className="td-flex">
                  <Button
                    id={teamId + "row" + props.i + "-btn-modifyRow"}
                    type="button"
                    className="btn-modify"
                    onClick={props.modifyHandler}
                  >
                    수정완료
                  </Button>
                  <Button
                    id={teamId + "row" + props.i + "-btn-delete"}
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
                id={teamId + "row" + props.i + "-btn-switchRow"}
                type="button"
                onClick={props.switchRowHanlder}
              >
                수정
              </Button>
            </td>
          ))}
      </tr>
    );
  } else if (props.version === "regist") {
    // bodyElement = props.data.map((row, i) => (
    return (
      <tr>
        {props.showNumber && <td>{props.i + 1}</td>}
        {props.row.isNew
          ? props.columns.map((col, j) => (
              <td key={"row" + props.i + "col" + j}>
                {inputField(col, props.row[col.id], props.i, j)}
              </td>
            ))
          : props.checkColumns.map((col, j) => (
              <td key={"row" + props.i + "col" + j}>
                {inputField(col, props.row[col.id], props.i, j)}
              </td>
            ))}
        <td>
          {props.row.isNew && (
            <Button
              id={props.teamId + "row" + props.i + "-btn-deleteRow"}
              type="button"
              onClick={props.deleteHandler}
            >
              삭제
            </Button>
          )}
        </td>
      </tr>
    );
  }
};

export default memo(RegistTableBodyElement);
