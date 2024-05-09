import React, { useEffect, useRef } from "react";

import Modal from "./Modal";
import Button from "../FormElements/Button";
import ErrorIcon from "@material-ui/icons/Error";

const ErrorModal = (props) => {
  const buttonRef = useRef(null); // 버튼에 대한 ref 생성
  const show = !!props.error;

  useEffect(() => {
    // 에러가 있고, 모달이 보여질 때 버튼에 포커스를 줍니다.
    if (show && buttonRef.current) {
      buttonRef.current.focus();
    }
  }, [show]); // show가 변할 때마다 실행

  return (
    <Modal
      onCancel={props.onClear}
      headerClass="modal__header-hide"
      contentClass="modal__content-flex"
      show={show}
      footer={
        <Button ref={buttonRef} onClick={props.onClear}>
          닫기
        </Button>
      }
    >
      <div className="error-icon">
        <ErrorIcon color="error" fontSize="medium" />
      </div>
      <div>{props.error}</div>
    </Modal>
  );
};

export default ErrorModal;
