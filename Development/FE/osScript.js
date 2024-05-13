const { exec } = require('child_process');

let command;

switch (process.platform) {
  case 'darwin': // macOS
    command = 'sudo react-scripts start';
    break;
  case 'win32': // Windows
    command = 'react-scripts start';
    break;
  case 'linux': // Linux
    command = 'sudo react-scripts start';
    break;
  default:
    command = 'react-scripts start';
}

exec(command, (error, stdout, stderr) => {
  if (error) {
    console.error(`exec error: ${error}`);
    return;
  }
  console.log(`stdout: ${stdout}`);
  if (stderr) {
    console.error(`stderr: ${stderr}`);
  }
});