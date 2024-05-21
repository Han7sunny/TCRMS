import React from "react";
import Modal from "../../../shared/components/UIElements/Modal";
import Button from "../../../shared/components/TableInputElements/Button";

import "./AddTeamModal.css";

const AddTeamModal = (props) => {
  return (
    <Modal
      onCancel={props.onClear}
      headerClass="modal__header-hide"
      show={props.show}
      footerClass="modal__footer-hide"
    >
      <div className="event-select-modal__title">
        단체전 종목 선택
      </div>
      <div className="event-select-modal__btns-wrap">
        <Button className="event-select-modal__btn">겨루기 남자</Button>
      </div>
    </Modal>
  );
};

export default AddTeamModal;
