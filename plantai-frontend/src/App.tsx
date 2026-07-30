import React, { useEffect, useRef, useState } from 'react';
import { sendChat } from './services/api';
import { ChatMessage, type Message } from './components/ChatMessage';
import { ChatInput } from './components/ChatInput';
import './App.css';
import { PottedPlantIcon } from "@phosphor-icons/react";

export default function App() {
    const [prompt, setPrompt] = useState('');
    const [messages, setMessages] = useState<Message[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const abortControllerRef = useRef<AbortController | null>(null);
    const chatBottomRef = useRef<HTMLDivElement | null>(null);

    const scrollToBottom = () => {
        chatBottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!prompt.trim() || loading) return;

        const userText = prompt.trim();
        setPrompt('');
        setError(null);
        setLoading(true);

        const userMsgId = Date.now().toString();
        const assistantMsgId = (Date.now() + 1).toString();

        setMessages((prev) => [
            ...prev,
            { id: userMsgId, role: 'user', content: userText },
            { id: assistantMsgId, role: 'assistant', content: '', isLoading: true },
        ]);

        const controller = new AbortController();
        abortControllerRef.current = controller;

        try {
            const responseText = await sendChat({
                prompt: userText,
                signal: controller.signal,
            });

            setMessages((prev) =>
                prev.map((msg) =>
                    msg.id === assistantMsgId
                        ? { ...msg, content: responseText, isLoading: false }
                        : msg
                )
            );
        } catch (err: any) {
            if (err.name === 'AbortError') {
                console.log('Interrupted by the user.');
                setMessages((prev) => prev.filter((msg) => msg.id !== assistantMsgId));
            } else {
                console.error('Request error:', err);
                setError(err.message || 'The server returned an error.');
                setMessages((prev) => prev.filter((msg) => msg.id !== assistantMsgId));
            }
        } finally {
            setLoading(false);
            abortControllerRef.current = null;
        }
    };

    const handleStop = () => {
        abortControllerRef.current?.abort();
    };

    return (
        <div style={styles.pageBackground}>
            <div style={styles.container}>
                <header style={styles.header}>
                    <div style={{ display: 'flex', gap: 8 }}>
                        <PottedPlantIcon size={24} color={'#7cc668'} weight="fill" />
                        <h2 style={styles.title}>PlantAI</h2>
                    </div>
                    <span style={styles.subtitle}>Your personal botanical assistant!</span>
                </header>

                <div style={styles.chatArea}>
                    {messages.length === 0 ? (
                        <div style={styles.emptyState}>
                            No messages yet.
                        </div>
                    ) : (
                        messages.map((msg) => <ChatMessage key={msg.id} message={msg} />)
                    )}
                    <div ref={chatBottomRef} />
                </div>

                {error && <div style={styles.errorBox}>{error}</div>}

                <div style={styles.inputWrapper}>
                    <ChatInput
                        prompt={prompt}
                        setPrompt={setPrompt}
                        loading={loading}
                        onSubmit={handleSubmit}
                        onStop={handleStop}
                    />
                </div>
            </div>
            <span style={styles.subtitle}>
                Created by Luis Fellipe Amaro. <a href={"https://github.com/amarogamedev/PlantAI"}>Read about this project on GitHub</a>
            </span>
        </div>
    );
}

const styles: { [key: string]: React.CSSProperties } = {
    pageBackground: {
        backgroundColor: '#101010',
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        gap: 32,
        justifyContent: 'center',
        alignItems: 'center',
        color: '#f8fafc',
        fontFamily: 'system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
        padding: '20px',
        boxSizing: 'border-box',
    },
    container: {
        width: '100%',
        maxWidth: '768px',
        height: '85vh',
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: '#202020',
        borderRadius: '16px',
        border: '1px solid #404040',
        boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.5)',
        overflow: 'hidden',
    },
    header: {
        padding: '16px 24px',
        borderBottom: '1px solid #404040',
    },
    title: {
        margin: 0,
        fontSize: '18px',
        fontWeight: 600,
        color: '#f8fafc',
    },
    subtitle: {
        fontSize: '12px',
        color: '#c4c4c4',
    },
    chatArea: {
        flex: 1,
        overflowY: 'auto',
        padding: '24px',
        display: 'flex',
        flexDirection: 'column',
    },
    emptyState: {
        margin: 'auto',
        color: '#c4c4c4',
        fontSize: '14px',
        textAlign: 'center',
    },
    errorBox: {
        margin: '0 24px 12px 24px',
        padding: '12px',
        backgroundColor: '#fca5a5',
        color: '#101010',
        borderRadius: '8px',
        fontSize: '14px',
    },
    inputWrapper: {
        padding: '16px',
        borderTop: '1px solid #404040',
    },
};