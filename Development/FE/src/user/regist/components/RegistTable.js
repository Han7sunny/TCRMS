import React, { useState } from "react";

// import Button from "../../../shared/components/TableInputElements/Button";

import "./RegistTable.css";
import RegistTableHeader from "./RegistTableHeader";
import RegistTableRow from "./RegistTableRow";

const RegistTable = (props) => {
  const teamId = props.teamId || "";

  const [hideText, setHideText] = useState(true);

  return (
    <table id={props.tableId} className="regist-table">
      <RegistTableHeader
        showNumber={props.showNumber}
        columns={props.columns}
        setHideText={setHideText}
        hideText={hideText}
        isEditable={props.isEditable}
      />
      <tbody>
        {props.data.map((row, i) => (
          <RegistTableRow
            key={i}
            version={props.version}
            teamId={teamId}
            rowData={row}
            rowIdx={i}
            showNumber={props.showNumber}
            columns={props.columns}
            modifyColumns={props.modifyColumns}
            checkColumns={props.checkColumns}
            isEditable={props.isEditable}
            hideText={hideText}
            inputHandler={props.inputHandler}
            buttonHandler={props.buttonHandler}
            modifyHandler={props.modifyHandler}
            deleteHandler={props.deleteHandler}
            switchRowHandler={props.switchRowHandler}
          />
        ))}
      </tbody>
    </table>
  );
};

export default RegistTable;
