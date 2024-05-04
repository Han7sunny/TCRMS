import React from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import RadioGroup from "../../../shared/components/TableInputElements/RadioGroup";
import CheckboxGroup from "../../../shared/components/TableInputElements/CheckboxGroup";
import Dropdown from "../../../shared/components/TableInputElements/Dropdown";
import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistIndividualTable.css";

function createData(
  name,
  sex,
  foreigner,
  nationality,
  idnumber,
  event,
  weight
) {
  return { name, sex, foreigner, nationality, idnumber, event, weight };
}

const rows = [
  createData(
    "홍길동",
    "남자",
    "true",
    "영국",
    "200101-2000000",
    ["겨루기"],
    "핀"
  ),
  createData("", "", "", "", "", [], ""),
];

const columns = [
  "",
  "성명",
  "성별",
  "외국인",
  "국적",
  "주민등록번호",
  "종목",
  "체급(겨루기만)",
  "",
];

const RegistIndividualTable = () => {
  const props = {
    columns: columns,
    data: rows,
  };

  const inputHandler = () => {};

  return (
    <table className="regist-table">
      <colgroup>
        {props.columns.map((col, i) => (
          <col key={col + i} className={"table-col-" + i} />
        ))}
      </colgroup>
      <thead>
        <tr>
          {props.columns.map((col, i) => (
            <th key={i}>{col}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {props.data.map((row, i) => (
          <tr key={row.name}>
            <td>{i + 1}</td>
            <td>{row.name}</td>
            <td>{row.sex}</td>
            <td>{row.sex}</td>
            <td>{row.nationality}</td>
            <td>{row.idnumber}</td>
            <td>{row.event}</td>
            <td>{row.weight}</td>
            <td></td>
          </tr>
        ))}
        <tr>
          <td>3</td>
          <td>
            <Input
              id="name"
              element="input"
              type="text"
              onInput={inputHandler}
              validators="[]"
              placeholder="성명"
            />
          </td>
          <td>
            <RadioGroup
              id="sex"
              items={["남성", "여성"]}
              initialValue="남성"
              onInput={inputHandler}
              showLabel
            />
          </td>
          <td>
            <CheckboxGroup
              id="foreigner"
              items={["외국인"]}
              onInput={inputHandler}
              showLabel
            />
          </td>
          <td>
            <Dropdown
              id="nationality"
              items={["대한민국", "영국", "프랑스"]}
              onInput={inputHandler}
            />
          </td>
          <td>
            <div className="div-flex">
              <Input
                id="idnum0"
                element="input"
                type="text"
                onInput={inputHandler}
                validators="[]"
              />
              &nbsp;-&nbsp;
              <Input
                id="idnum1"
                element="input"
                type="text"
                onInput={inputHandler}
                validators="[]"
              />
            </div>
          </td>
          <td>
            <CheckboxGroup
              id="event"
              items={["겨루기", "품새"]}
              onInput={inputHandler}
              showLabel
            />
          </td>
          <td>
            <Dropdown
              id="nationality"
              items={[
                "핀",
                "플라이",
                "밴텀",
                "페더",
                "라이트",
                "웰터",
                "미들",
                "헤비",
              ]}
              onInput={inputHandler}
            />
          </td>
          <td>
            <Button>삭제</Button>
          </td>
        </tr>
      </tbody>
    </table>
  );
};

export default RegistIndividualTable;
