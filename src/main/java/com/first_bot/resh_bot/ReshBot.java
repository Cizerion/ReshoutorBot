package com.first_bot.resh_bot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ReshBot extends TelegramLongPollingBot{
	private static final String BOTNAME = "[BOT]";
	private static final String BOTTOKEN = "[TOKEN]";
	private static final String THUMBNAIL = "https://cdn.pixabay.com/photo/2016/05/31/12/24/speech-bubble-1426772_960_720.png";
	private Message message;
	private String queryMessage;
	private Update update;
	private String[] replies = { "Не знаю. Спроси у друзей лучше. \uD83D\uDE09",
		"Купи мне слона! \uD83D\uDC18 Хаха, но по твоему вопросу - думаю да.",
		"Ммм нет, лучше ложись спать. \uD83D\uDE34 ДА, ПРЯМО СЕЙЧАС " + getDate() + "\u2757",
		"Ой всё! \uD83D\uDE46",
		"Ты человек, поэтому да. \uD83D\uDE00",
		"Я бот \uD83D\uDEB7, поэтому нет.",
		"Не хочу! Не буду! Ладно, решу я твой вопрос. Спроси меня еще раз. \uD83D\uDE4A",
		"Да, и знаешь что? Как говорится: \"Ты красавчик!\" \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25",
		"Не, иди лучше выпей чаю, друг. \uD83D\uDE0E",
		"У меня скрипт болит, давай завтра это обсудим? \uD83D\uDE10",
		"Давай, даю тебе своё скомпилированное согласие! \uD83D\uDE0E",
		"Знаешь, я не отвечу тебе на твой вопрос, но знай - ты лучший! \uD83D\uDE0E\uD83D\uDE03\uD83D\uDE0E",
		"Не могу ответить, сложно. НО! Если б не был ботом я, то добавился б в друзья! \uD83D\uDCBE",
		"Однажды, в далёкой-далёкой галактике, когда кто-то задался вопросом, Решатор дал на него ответ: да. \uD83D\uDE80\u2B50",
		"Я же сказал, никаких серьёзных вопросов! Поэтому нет! Или давай по новой... \uD83D\uDE25",
		"Ну что такое, снова вопрос? Давай лучше в приставку поиграем! \uD83C\uDFAE",
		"Интересный вопрос. Тогда я запускаю бота Решатора, который запускает бота Решатора, который... В общем ответ: да. \uD83D\uDD2E",
		"Не, не думай об этом. Лучше закинь лукас моему создателю! \uD83D\uDC4D"};
	
	public void onUpdateReceived(Update update) {
		this.update = update;
		try {
			if(update.hasInlineQuery()) {
				handleInlineQuery(update.getInlineQuery());
			}
			if(update.hasMessage()){
				handleIncomingMessage(update);
		    } else if(update.hasCallbackQuery()){
		    	handleCallbackQuery(update);
		    }
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void handleIncomingMessage(Update update) throws TelegramApiException {
		if(update.getMessage().hasText() && update.getMessage().getText().startsWith("/")){
			message = update.getMessage();
    		System.out.println(message.getText());
    		if(update.getMessage().getText().equals("/author")) {
        		execute(new SendMessage()
        				.setText("Хэй, раз ты ввёл эту команду, то знай это меня Стасон (aka Claner/Cizerion) запилил!")
        				.setChatId(update.getMessage().getChatId()));
        	}
			if(update.getMessage().getText().startsWith("/q")) {
        		if(message.getText().length() >= 7) {
        			execute(setInlineButtons(update.getMessage().getChatId()));
            	} else {
            		execute(new SendMessage()
            				.setText("Чот коротенько как-то, Бро/Сис. Давай-ка ты задашь вопрос подлинее. " 
            											+ "Или спроси как задать вопрос у меня по хелпу: /help")
            				.setChatId(update.getMessage().getChatId()));
            	}
        	}
			if(message.getText().equals("/start")) {
        		execute(new SendMessage()
        				.setText("И еще раз привет, " + message.getFrom().getFirstName() + "\u2757"
        						+ "\nМеня зовут Решатор, бот с которым стоит подружиться! Давай дружить! Ты можешь задать мне любой вопрос и я на него дам интересный (или скучный) ответ."
        						+ " Самое \u203C ГЛАВНОЕ \u203C, друг, не доверяй мне сложных и отвественных вопросов и задач! Лучше давай решать вопросы связанные, к примеру, с выбором "
        						+ "блюд на ужин, какой посмотреть фильм или сериал, попить или не попить чай, купить яблоки красные или жёлтые и т.д."
        						+ " Запомни, ты - ЧЕЛОВЕК \uD83D\uDCAA, только ты сам несёшь ответсвенность за себя и свои действия! А я, пожалуй, говорящей монеткой поинтерпретируюсь: Сэр, есть Сэр! Ваш хелп, Сэр: /help!")
        				.setChatId(update.getMessage().getChatId()));
        	}
        	if(message.getText().equals("/help")) {
        		execute(new SendMessage()
        				.setText(getHelp())
        				.setChatId(update.getMessage().getChatId()));
        	}
        }
	}
	
	private void handleCallbackQuery(Update update) throws TelegramApiException {
		if(message.getText().length() >= 7 && !message.getText().equals("/author") && update.getCallbackQuery().getData().equals("Ну вот, начинается. Ладно! \uD83D\uDC4C")) {
			execute(new SendMessage()
					.setText(update
							.getCallbackQuery()
							.getData())
					.setChatId(update
							.getCallbackQuery()
							.getMessage()
							.getChatId()));
			execute(setInlineButtons(message.getChatId()));
    	} else if(update.getCallbackQuery().getData().equals("Вот и славненько! \uD83D\uDE0C")){
    		execute(new SendMessage()
    				.setText(update
    						.getCallbackQuery()
    						.getData())
    				.setChatId(update
    						.getCallbackQuery()
    						.getMessage()
    						.getChatId()));
    	} else {
    		execute(new SendMessage()
    				.setText("Бро/Сис, введи новый вопрос лучше. А вот и хелп в помощь: /help")
    				.setChatId(update
    						.getCallbackQuery()
    						.getMessage()
    						.getChatId()));
    	}
	}
	
	private SendMessage setInlineButtons(long chatId) {
		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		
		List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ок \uD83D\uDC4D").setCallbackData("Вот и славненько! \uD83D\uDE0C"));
		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Давай по новой \u2B50").setCallbackData("Ну вот, начинается. Ладно! \uD83D\uDC4C"));
		
		List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
		rowList.add(keyboardButtonsRow1);
		
		markupKeyboard.setKeyboard(rowList);
		
		return new SendMessage()
				.setChatId(chatId)
				.setText(message.getFrom().getFirstName() + ", на твой вопрос \"" + message.getText().substring(2, message.getText().length()) + "\" мой ответ: \n\n" + randomSolution())
				.setReplyMarkup(markupKeyboard);
	}
	
	private void handleInlineQuery(InlineQuery inlineQuery) throws TelegramApiException {
		queryMessage = inlineQuery.getQuery();
		String message = inlineQuery.getQuery();
		System.out.println(message);
		if(message.length() < 100) {
			if(!message.isEmpty() && message.endsWith("#")) {
				execute(getResultsToResponse(inlineQuery, getRandomResult()));
			} else {
				execute(getResultsToResponse(inlineQuery, getHelpResult()));
			}
		} else {
			execute(getResultsToResponse(inlineQuery, getHelpResult()));
		}
	}
	
	private AnswerInlineQuery getResultsToResponse(InlineQuery inlineQuery, InlineQueryResult result) {
		AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
		answerInlineQuery.setInlineQueryId(inlineQuery.getId());
		answerInlineQuery.setResults(result);
		return answerInlineQuery;
	}
	
	private InlineQueryResult getRandomResult() {
		InputTextMessageContent messageContent = new InputTextMessageContent();
		messageContent.disableWebPagePreview();
		messageContent.enableMarkdown(true);
		messageContent.setMessageText(getUpdate().getInlineQuery().getFrom().getFirstName() + ", на твой вопрос \"" + queryMessage + "\" мой ответ: \n\n" + randomSolution());
		
		InlineQueryResultArticle answer = new InlineQueryResultArticle();
		answer.setInputMessageContent(messageContent);
		answer.setId("1");
		answer.setTitle("Узнать ответ на свой вопрос!");
		answer.setDescription(queryMessage);
		answer.setThumbUrl(THUMBNAIL);
		
		return answer;
	}
	
	private InlineQueryResult getHelpResult() {
		InputTextMessageContent messageContent = new InputTextMessageContent();
		messageContent.disableWebPagePreview();
		messageContent.enableMarkdown(true);
		messageContent.setMessageText(getHelp());
		
		InlineQueryResultArticle help = new InlineQueryResultArticle();
		help.setInputMessageContent(messageContent);
		help.setId("2");
		help.setTitle("Решатор решит!");
		help.setDescription("Закончи свой вопрос знаком \"#\". Или жмякай сюда для вызова справки!");
		help.setThumbUrl(THUMBNAIL);
		
		return help;
	}
	
	private String getHelp() {
		if(getUpdate().hasMessage()) {
			return "\u203C\u203C ВАЖНО \u203C\u203C НИКТО КРОМЕ ВАС не несёт ответственность за Ваш выбор, кто бы или что бы Вам ни советовали! \u203C\u203C"
					+ "\n\nУ Решатора есть поддержка inline ввода (т.е. в строке ввода) - * @reshoutor_bot [вопрос] *."
					+ "\nВ этом режиме обязательно напиши знак решётки: # - на конце своего вопроса!"
					+ "\n\nНо Решатор может ответить на твой вопрос и в приватном чате! Просто обратись к нему и начни чат - жмякай сюда => @reshoutor_bot."
					+ "\nВ чате с Решатором ты можешь пользоваться следующими командами:"
					+ "\n/help - Вызов справки \uD83C\uDF93"
					+ "\n/start - Поприветствовать решатора \uD83D\uDC4B"
					+ "\n/q [вопрос] - Задать Решатору вопрос \u2753";
		} else if(getUpdate().hasInlineQuery()){
			return "\u203C\u203C ВАЖНО \u203C\u203C НИКТО КРОМЕ ВАС не несёт ответственность за Ваш выбор, кто бы или что бы Вам ни советовали! \u203C\u203C"
					+ "\n\nУ Решатора есть поддержка inline ввода (т.е. в строке ввода) - @reshoutor\\_bot [вопрос]."
					+ "\nВ этом режиме обязательно напиши знак решётки: # - на конце своего вопроса!"
					+ "\n\nНо Решатор может ответить на твой вопрос и в приватном чате! Просто обратись к нему и начни чат - жмякай сюда => @reshoutor\\_bot."
					+ "\nВ чате с Решатором ты можешь пользоваться следующими командами:"
					+ "\n/help - Вызов справки \uD83C\uDF93"
					+ "\n/start - Поприветствовать решатора \uD83D\uDC4B"
					+ "\n/q [вопрос] - Задать Решатору вопрос \u2753";
		} else {
			return "Что-то не так. Скажи об этом Стасону!";
		}
	}
	
	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	private String randomSolution() {
		return replies[(int)(Math.random() * 18)];
	}
	
	private Update getUpdate() {
		return update;
	}
	
	public String getBotUsername() {
		return BOTNAME;
	}

	@Override
	public String getBotToken() {
		return BOTTOKEN;
	}
}
