import React from "react";

import Modal from "./Modal";
import Button from "../FormElements/Button";

const ErrorModal = (props) => {
  return (
    <Modal
      onCancel={props.onClear}
      // header="오류 발생"
      headerClass="modal__header-hide"
      show={!!props.error}
      footer={<Button onClick={props.onClear}>확인</Button>}
    >
      <p>{props.error}</p>
    </Modal>
  );
};

export default ErrorModal;
