import React from "react";

import Input from "../../../shared/components/TableInputElements/Input";
import Radio from "../../../shared/components/TableInputElements/Radio";

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
            <Radio />
          </td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
      </tbody>
    </table>
  );
};

export default RegistIndividualTable;
