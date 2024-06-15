# Gen AI Training Application

## Overview

This application integrates Azure's OpenAI chat completion service to demonstrate AI-driven chat functionalities. It
includes endpoints for sending prompts and receiving AI-generated responses, as well as using advanced features like
function messages and semantic kernels.

## Configuration

The application is configured to use Azure OpenAI services. To set up your local development environment:

Configure the Azure OpenAI API key, endpoint, and deployment name in `src/main/resources/config/application.properties`:

   ```properties
   client-azureopenai-key=your-key
client-azureopenai-endpoint=https://your.ai-api.com/
client-azureopenai-deployment-name=gpt-xx-turbo
   ```

## Running the Application

Run the application using Spring Boot:

```bash
mvn spring-boot:run
```

## API Endpoints

### Simple Chat Prompt

- **Description**: Get AI responses for simple text prompts.
- **Method**: `GET`
- **URL**: `/chat/simple?input=your-prompt`
- **Example**:
  ```http
  GET http://localhost:8080/chat/simple?input=I%20want%20to%20find%20top-10%20books%20about%20world%20history
  ```

### Function Message with Default Settings

- **Description**: Use a function message for context with default settings.
- **Method**: `GET`
- **URL**: `/chat/function-message?input=I%20want%20to%20find%20top-10%20books%20about%20world%20history`
- **Example**:
  ```http
  GET http://localhost:8080/chat/function-message?input=I%20want%20to%20find%20top-10%20books%20about%20world%20history
  ```

### Function Message with Changed Settings

- **Description**: Use a function message for context with modified settings.
- **Method**: `GET`
- **URL**: `/chat/function-message-changed?input=I%20want%20to%20find%20top-10%20books%20about%20world%20history`
- **Example**:
  ```http
  GET http://localhost:8080/chat/function-message-changed?input=I%20want%20to%20find%20top-10%20books%20about%20world%20history
  ```

### Kernel Simple Prompt Response

- **Method**: `POST`
- **URL**: `/kernel/simple`
- **Body**:
  ```json
  { "input": "I want to find top-10 books about world history" }
  ```
- **Example**:
  ```http
  POST http://localhost:8080/kernel/simple
  ```

### Kernel JSON Prompt Response

- **Method**: `POST`
- **URL**: `/kernel/json`
- **Body**:
  ```json
  { "input": "I want to find top-10 books about world history" }
  ```
- **Example**:
  ```http
  POST http://localhost:8080/kernel/json
  ```

## Additional Resources

- API Tests: Located in the `api_tests` folder, includes HTTP requests (`gen_ai_training_m1_api_requests.http`) and
  Postman collection (`gen_ai_training_m1_postman_collection.json`).
