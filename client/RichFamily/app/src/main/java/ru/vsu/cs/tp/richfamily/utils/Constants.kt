package ru.vsu.cs.tp.richfamily.utils

object Constants {
    const val WALLET_UPDATE: String = "Счет изменен"
    const val SUBMIT_WAL_DELETE_TEXT: String = "При удалении счета, также удалятся все расходы и доходы, " +
            "записанные на этот счет.\nВы уверены?"
    const val SUBMIT_CAT_DELETE_TEXT: String = "При удалении категории, также удалятся все расходы и доходы, " +
            "в которых используется данная категория.\nВы уверены?"
    const val CONS_TEXT = "РАСХОД"
    const val INCOME_TEXT = "ДОХОД"
    const val NAME_PLACEHOLDER = "Лучший пользователь"
    const val EMAIL_PLACEHOLDER = "Здесь пока что пусто"
    const val COMP_FIELDS_TOAST = "Заполните все поля!"
    const val COMP_FIELD = "Поле не должно быть пустым!"
    const val SUCCESS = "Успешно!"
    const val FIRSTPAY_BT_SUM = "Некорректный первоначальный взнос!"
    const val DELETE_GROUP_MESSAGE = "При удалении группы все пользваотели автоматически выйдут из нее," +
            " их доходы и расходы будут сохранены\nВы уверены?"
    const val LEAVE_GROUP_MESSAGE = "Вы уверены?"
    const val EXIT_ACCOUNT_MESSAGE = "Вы действительно хотите выйти?"
    const val NO_SUCH_USER = "Ошибка при добавление пользователя"
    const val USER_EXISTS = "Пользовтель с такой почтой уже существует!"
    const val WALLET_INVALID = "Неверно заполнено поле!"
    const val CANT_LOGIN = "Невозможно войти с предоставленными данными!"
    const val PWD_NOT_COMPARE = "Пароли не совпадают!"
    const val PWD_INVALID = "Пароль должен содержать не менее 8 символов, как минимум одну заглавную букву и цифру!"
    const val INVALID_EMAIL = "Неверно заполнено поле email!"
    const val SUCCESS_LOGIN = "Успешно!"
    const val INVALID_DATA = "Неверные данные"
    const val SUCCESS_TOAST = "Успешно!"
    const val PARTICIPANT = "Участник"
    const val LEADER = "Лидер"
    const val INVALID_GROUP_NAME = "Недопустимое название группы!"
    const val SUBMIT_GROUP_USER_DELETE_TEXT = "При удалении пользователя, все расходы и доходы," +
            " созданные им во время участия в группе будут сохранены. " +
            "Вы больше не сможете увидеть операции пользователя.\nПродолжить?"
}