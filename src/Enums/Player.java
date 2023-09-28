package Enums;

public enum Player {
    PLAYER_ONE {
        @Override
        public Player next() {
            return Player.PLAYER_TWO;
        }
    },
    PLAYER_TWO {
        @Override
        public Player next() {
            return Player.PLAYER_ONE;
        }
    };
    public abstract Player next();
}
