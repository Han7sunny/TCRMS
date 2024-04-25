import React, { useState, useContext } from "react";

// import Card from "../../shared_/components/UIElements/Card";
import Input from "../../shared_/components/FormElements/Input";
import Button from "../../shared_/components/FormElements/Button";
import ErrorModal from "../../shared_/components/UIElements/ErrorModal";
import LoadingSpinner from "../../shared_/components/UIElements/LoadingSpinner";
import ImageUpload from "../../shared_/components/FormElements/ImageUpload";
import {
  VALIDATOR_EMAIL,
  VALIDATOR_MINLENGTH,
  VALIDATOR_REQUIRE,
} from "../../shared_/util/validators";
import { useForm } from "../../shared_/hooks/form-hook";
import { useHttpClient } from "../../shared_/hooks/http-hook";
import { AuthContext } from "../../shared_/context/auth-context";
import "./Login.css";

const Login = () => {
  const auth = useContext(AuthContext);
  const [isFirst, setIsFirst] = useState(true);
  const { isLoading, error, sendRequest, clearError } = useHttpClient();

  const [formState, inputHandler, setFormData] = useForm(
    {
      email: {
        value: "",
        isValid: false,
      },
      password: {
        value: "",
        isValid: false,
      },
    },
    false
  );

  const switchModeHandler = () => {
    if (!isFirst) {
      setFormData(
        {
          ...formState.inputs,
          name: undefined,
          image: undefined,
        },
        formState.inputs.email.isValid && formState.inputs.password.isValid
      );
    } else {
      setFormData(
        {
          ...formState.inputs,
          name: {
            value: "",
            isValid: false,
          },
          image: {
            value: null,
            isValid: false,
          },
        },
        false
      );
    }
    setIsFirst((prevMode) => !prevMode);
  };

  const authSubmitHandler = async (event) => {
    event.preventDefault();

    if (isFirst) {
      try {
        const responseData = await sendRequest(
          "http://localhost:5000/api/users/login",
          "POST",
          JSON.stringify({
            email: formState.inputs.email.value,
            password: formState.inputs.password.value,
          }),
          {
            "Content-Type": "application/json",
          }
        );
        auth.login(responseData.userId, responseData.token);
      } catch (err) {}
    } else {
      try {
        const formData = new FormData();
        formData.append("email", formState.inputs.email.value);
        formData.append("name", formState.inputs.name.value);
        formData.append("password", formState.inputs.password.value);
        formData.append("image", formState.inputs.image.value);
        const responseData = await sendRequest(
          "http://localhost:5000/api/users/signup",
          "POST",
          formData
        );

        auth.login(responseData.userId, responseData.token);
      } catch (err) {}
    }
  };

  return (
    <React.Fragment>
      <ErrorModal error={error} onClear={clearError} />
      <div className="authentication">
        <div>
          <div>
            <img
              src={`${process.env.PUBLIC_URL}/img/KUTCA_logo.png`}
              alt=""
              width="200px"
            />
          </div>
          <div className="authentication-form">
            {isLoading && <LoadingSpinner asOverlay />}
            <form onSubmit={authSubmitHandler}>
              {/* {!isFirst && (
                <Input
                  element="input"
                  id="name"
                  type="text"
                  label="Your Name"
                  validators={[VALIDATOR_REQUIRE()]}
                  errorText="Please enter a name."
                  onInput={inputHandler}
                />
              )} */}
              <Input
                element="input"
                id="uni-name"
                type="text"
                placeholder="학교명"
                validators={[VALIDATOR_EMAIL()]}
                // errorText="Please enter a valid email address."
                onInput={inputHandler}
              />
              <Input
                element="input"
                id="user-name"
                type="text"
                placeholder="대표자명"
                validators={[VALIDATOR_EMAIL()]}
                // errorText="Please enter a valid email address."
                onInput={inputHandler}
              />
              <Input
                element="input"
                id="password"
                type="password"
                placeholder="비밀번호"
                validators={[VALIDATOR_MINLENGTH(6)]}
                errorText="Please enter a valid password, at least 6 characters."
                onInput={inputHandler}
              />
              <Button type="submit" disabled={!formState.isValid}>
                {isFirst ? "로그인" : "비밀번호 변경"}
              </Button>
            </form>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Login;
