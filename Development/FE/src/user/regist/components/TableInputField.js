import React from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";

const inputField = (
  colInfo,
  initVal,
  rowidx,
  colidx,
  key,
  inputHandler,
  buttonHandler,
  teamId,
  hideText
) => {
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
          onInput={inputHandler}
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
          onInput={inputHandler}
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
          onInput={inputHandler}
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
          onInput={inputHandler}
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
          onClick={buttonHandler}
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
              inputHandler,
              buttonHandler,
              teamId,
              hideText
            )
          )}
        </div>
      );
    default:
  }
};

export default inputField;
