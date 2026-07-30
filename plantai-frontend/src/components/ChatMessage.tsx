import React from 'react';
import Markdown from "react-markdown";
import remarkBreaks from "remark-breaks";
import {HourglassHighIcon} from "@phosphor-icons/react";

export interface Message {
    id: string;
    role: 'user' | 'assistant';
    content: string;
    isLoading?: boolean;
}

interface ChatMessageProps {
    message: Message;
}

export const ChatMessage: React.FC<ChatMessageProps> = ({ message }) => {
    const isUser = message.role === 'user';

    return (
        <div style={{ ...styles.row, justifyContent: isUser ? 'flex-end' : 'flex-start' }}>
            <div style={{ ...styles.bubble, ...(isUser ? styles.userBubble : styles.assistantBubble) }}>
                <div style={styles.sender}>{isUser ? 'You' : 'PlantAI Assistant'}</div>
                <div style={styles.text}>
                    <Markdown
                        components={{
                            p: ({ children }) => <p style={styles.p}>{children}</p>,
                            ol: ({ children }) => <ol style={styles.list}>{children}</ol>,
                            ul: ({ children }) => <ul style={styles.list}>{children}</ul>,
                            li: ({ children }) => <li style={styles.listItem}>{children}</li>,
                            strong: ({ children }) => <strong style={styles.strong}>{children}</strong>,
                        }}
                        remarkPlugins={[remarkBreaks]}
                    >
                        {message.content}
                    </Markdown>
                    {message.isLoading && <HourglassHighIcon size={24} color={'#101010'} weight="fill" />}
                </div>
            </div>
        </div>
    );
};

const styles: { [key: string]: React.CSSProperties } = {
    row: {
        display: 'flex',
        marginBottom: '16px',
        width: '100%',
    },
    bubble: {
        maxWidth: '75%',
        padding: '12px 16px',
        borderRadius: '16px',
        fontSize: '15px',
        lineHeight: '1.5',
        wordBreak: 'break-word',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.2)',
    },
    userBubble: {
        backgroundColor: '#7cc668',
        color: '#101010',
        borderBottomRightRadius: '4px',
    },
    assistantBubble: {
        backgroundColor: '#101010',
        color: '#ffffff',
        borderBottomLeftRadius: '4px',
        border: '1px solid #404040',
    },
    sender: {
        fontSize: '11px',
        fontWeight: 600,
        marginBottom: '4px',
        opacity: 0.7,
        textTransform: 'uppercase',
        letterSpacing: '0.5px',
    },
    text: {},
    p: {
        margin: '0 0 8px 0',
    },
    list: {
        marginTop: '4px',
        marginBottom: '8px',
        paddingLeft: '20px',
    },
    listItem: {
        marginBottom: '4px',
    },
    strong: {
        fontWeight: 600,
    }
};