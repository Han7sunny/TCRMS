export function deepEqual(obj1, obj2) {
  // Check if they are the same object reference
  if (obj1 === obj2) {
    return true;
  }

  // Check if they are both objects or arrays
  if (
    typeof obj1 !== "object" ||
    obj1 === null ||
    typeof obj2 !== "object" ||
    obj2 === null
  ) {
    return false;
  }

  // Check if they have the same number of keys or length
  const keys1 = Object.keys(obj1);
  const keys2 = Object.keys(obj2);
  if (keys1.length !== keys2.length) {
    return false;
  }

  // Recursively compare nested objects or arrays
  for (let key of keys1) {
    if (!deepEqual(obj1[key], obj2[key])) {
      return false;
    }
  }

  return true;
}
