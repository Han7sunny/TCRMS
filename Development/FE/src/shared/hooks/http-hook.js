import { useState, useCallback, useRef, useEffect, useContext } from "react";

import { AuthContext } from "../context/auth-context";

export const useHttpClient = () => {
  const auth = useContext(AuthContext);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState({ title: null, detail: null });

  const activeHttpRequests = useRef([]);

  const sendRequest = useCallback(
    async (
      url,
      method = "GET",
      body = null,
      headers = {},
      errorModalTitle = null
    ) => {
      setIsLoading(true);
      const httpAbortCtrl = new AbortController();
      activeHttpRequests.current.push(httpAbortCtrl);

      try {
        const response = await fetch(url, {
          method,
          body,
          headers,
          signal: httpAbortCtrl.signal,
        });

        const responseData = await response.json();

        console.log(responseData);

        activeHttpRequests.current = activeHttpRequests.current.filter(
          (reqCtrl) => reqCtrl !== httpAbortCtrl
        );

        if (response.status === 401) {
          auth.logout();
          throw new Error(responseData.message);
        }

        if (!response.ok || !responseData.isSuccess) {
          throw new Error(responseData.message);
        }

        setIsLoading(false);
        return responseData;
      } catch (err) {
        setError({ title: errorModalTitle, detail: err.message });
        setIsLoading(false);
        throw err;
      }
    },
    [auth]
  );

  const clearError = () => {
    setError({ title: null, detail: null });
  };

  useEffect(() => {
    return () => {
      // eslint-disable-next-line react-hooks/exhaustive-deps
      activeHttpRequests.current.forEach((abortCtrl) => abortCtrl.abort());
    };
  }, []);

  return { isLoading, error, sendRequest, setError, clearError };
};
