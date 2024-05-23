import React from "react";
import Modal from "../../../shared/components/UIElements/Modal";
import Button from "../../../shared/components/TableInputElements/Button";

import "./AddTeamModal.css";

import { teamInitialFormat } from "../../../shared/util/regist-team-defaultFormat";

const AddTeamModal = (props) => {
  const eventNameArr = [
    "겨루기 남성",
    "겨루기 여성",
    "품새 남성",
    "품새 여성",
    "품새 페어",
  ];

  const onClick = (eventName) => {
    props.onClick(teamInitialFormat(eventName));
  };

  return (
    <Modal
      onCancel={props.onClear}
      headerClass="modal__header-hide"
      contentClass="add-team-modal__content"
      show={props.show}
      footerClass="modal__footer-hide"
      className="event-select-modal"
    >
      <div className="event-select-modal__title">단체전 종목 선택</div>
      {/* <div className="event-select-modal__btns-wrap"> */}
      {eventNameArr.map((eventName) => (
        <Button
          key={eventName}
          id={eventName}
          className="event-select-modal__btn"
          onClick={() => {
            onClick(eventName);
          }}
        >
          {eventName}
        </Button>
      ))}
      {/* </div> */}
    </Modal>
  );
};

export default AddTeamModal;
