import React from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistTable.css";

const RegistTable = (props) => {
  const inputField = (colInfo, initVal, rowidx, colidx, key) => {
    switch (colInfo.type) {
      case "text":
        return (
          <div
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
          >
            &nbsp;{initVal}&nbsp;
          </div>
        );
      case "input":
        return (
          <Input
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            element="input"
            type="text"
            onInput={props.inputHandler}
            validators={colInfo.detail.validators}
            placeholder={colInfo.detail.placeholder}
            initialValue={initVal}
          />
        );
      case "radio-group":
        return (
          <RadioGroup
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            initialValue={initVal}
            onInput={props.inputHandler}
            showLabel={colInfo.detail.showLabel}
          />
        );
      case "checkbox-group":
        return (
          <CheckboxGroup
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            initialValue={initVal}
            onInput={props.inputHandler}
            showLabel={colInfo.detail.showLabel}
            affector={colInfo.detail.affector}
          />
        );
      case "dropdown":
        return (
          <Dropdown
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            items={colInfo.detail.items}
            onInput={props.inputHandler}
            initialValue={initVal}
          />
        );
      case "button":
        return (
          <Button
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
            onClick={props.buttonHandler}
          >
            {colInfo.detail.content}
          </Button>
        );
      case "multi-input":
        return (
          <div
            className="div-flex"
            id={"row" + rowidx + "-col" + colidx + "-" + colInfo.id}
            key={key}
          >
            {colInfo.details.map((item, i) =>
              inputField(item, initVal[i], rowidx, colidx, colInfo.id + i)
            )}
          </div>
        );
      default:
    }
  };

  return (
    <table className="regist-table">
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
          {props.columns.map((col) => (
            <th key={col.id}>{col.name}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {props.data.map((row, i) => (
          <tr key={i}>
            {props.showNumber && <td>{i + 1}</td>}
            {props.columns.map((col, j) => (
              <td key={"row" + i + "col" + j}>
                {inputField(col, row[col.id], i, j)}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default RegistTable;
