package net.kineticraft.lostcity.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ComponentBuilderWrapper {
    private final ComponentBuilder builder;

    public ComponentBuilderWrapper(ComponentBuilder original) {
        this.builder = new ComponentBuilder(original);
    }

    public ComponentBuilderWrapper(String text) {
        this.builder = new ComponentBuilder(text);
    }

    public ComponentBuilderWrapper append(BaseComponent[] components) {
        builder.append(components);
        return this;
    }

    public ComponentBuilderWrapper append(BaseComponent[] components, ComponentBuilder.FormatRetention retention) {
        builder.append(components, retention);
        return this;
    }

    public ComponentBuilderWrapper append(String text) {
        builder.append(text);
        return this;
    }

    public ComponentBuilderWrapper append(String text, ComponentBuilder.FormatRetention retention) {
        builder.append(text, retention);
        return this;
    }

    public ComponentBuilderWrapper color(ChatColor color) {
        builder.color(color);
        return this;
    }

    public ComponentBuilderWrapper bold(boolean bold) {
        builder.bold(bold);
        return this;
    }

    public ComponentBuilderWrapper italic(boolean italic) {
        builder.italic(italic);
        return this;
    }

    public ComponentBuilderWrapper underlined(boolean underlined) {
        builder.underlined(underlined);
        return this;
    }

    public ComponentBuilderWrapper strikethrough(boolean strikethrough) {
        builder.strikethrough(strikethrough);
        return this;
    }

    public ComponentBuilderWrapper obfuscated(boolean obfuscated) {
        builder.obfuscated(obfuscated);
        return this;
    }

    public ComponentBuilderWrapper insertion(String insertion) {
        builder.insertion(insertion);
        return this;
    }

    public ComponentBuilderWrapper event(ClickEvent event) {
        builder.event(event);
        return this;
    }

    public ComponentBuilderWrapper event(HoverEvent event) {
        builder.event(event);
        return this;
    }

    public ComponentBuilderWrapper reset() {
        builder.reset();
        return this;
    }

    public ComponentBuilderWrapper retain(ComponentBuilder.FormatRetention retention) {
        builder.retain(retention);
        return this;
    }

    public ComponentBuilder unwrap() {
        return builder;
    }

    public BaseComponent[] create() {
        return builder.create();
    }
}

