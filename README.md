# 🌱 PlantAI

PlantAI is a simple prototype project built to learn and experiment with **Spring AI** using Java. The project is an AI-powered plant assistant that uses a local LLM to help manage plant information through natural language.

## 🚀 Technologies

* Java 21
* Spring Boot 3
* Spring AI 1.0.9
* React + TypeScript
* MySQL
* Ollama
* Qwen3:8B

## 🧠 Features

* AI chat assistant powered by a local LLM
* Spring AI integration with `ChatClient`
* Tool Calling for plant management actions
* Conversation memory
* Streaming responses using SSE
* CRUD operations for plants

## ⚙️ Running

Install [Ollama](https://ollama.com/), download and run the model:

```bash
ollama pull qwen3:8b
ollama run qwen3:8b
```

Start the backend:

```bash
./mvnw spring-boot:run
```

Start the frontend:

```bash
npm install
npm run dev
```
