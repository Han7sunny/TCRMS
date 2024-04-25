import React from "react";

import UsersList from "../components/UsersList";

const Users = () => {
  const USERS = [
    {
      id: "u1",
      name: "seoyeong",
      image:
        "https://file.f-lab.kr/blog/53b15078-6fa9-457f-90ac-9794cd295d16-pxBsYWZWug016rkd.jpg",
      places: 3,
    },
  ];

  return <UsersList items={USERS} />;
};

export default Users;
