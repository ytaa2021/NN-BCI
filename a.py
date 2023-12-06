import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv1D, MaxPooling1D, Flatten, Dense
from tensorflow.keras.utils import to_categorical

# Load the data
df = pd.read_csv('labeled-data.csv')

# Remove examples labeled 'Done'
df_filtered = df[df['label'] != 'Done']

# Extract features and labels
X = df_filtered.iloc[:, 2:11].values
y = df_filtered['label'].values

# Standardize the data
scaler = StandardScaler()
X = scaler.fit_transform(X)

# Encode labels for binary classification (N vs. non-N)
class_order_binary = ['N', 'Non-N']
label_encoder_binary = LabelEncoder()
label_encoder_binary.fit(class_order_binary)
y_encoded_binary = label_encoder_binary.transform(df_filtered['label'])

# Convert labels to categorical
y_one_hot_binary = to_categorical(y_encoded_binary, num_classes=4)

# Split the data into train and test sets for binary classification
X_train_binary, X_test_binary, y_train_binary, y_test_binary = train_test_split(
    X, y_one_hot_binary, test_size=0.1, random_state=42
)

# Reshape the data for Conv1D layer
X_train_binary = X_train_binary.reshape(X_train_binary.shape[0], X_train_binary.shape[1], 1)
X_test_binary = X_test_binary.reshape(X_test_binary.shape[0], X_test_binary.shape[1], 1)

# Build the CNN model for binary classification
model_binary = Sequential()
model_binary.add(Conv1D(filters=32, kernel_size=3, activation='relu', input_shape=(X_train_binary.shape[1], 1)))
model_binary.add(MaxPooling1D(pool_size=2))
model_binary.add(Flatten())
model_binary.add(Dense(10, activation='relu'))
model_binary.add(Dense(2, activation='softmax'))  # Output layer for binary classification

# Compile the model
model_binary.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Train the model
model_binary.fit(X_train_binary, y_train_binary, epochs=10, batch_size=32, validation_split=0.2)

# Evaluate the model on the test set for binary classification
accuracy_binary = model_binary.evaluate(X_test_binary, y_test_binary)[1]
print(f'Binary Classification Test Accuracy: {accuracy_binary}')

# Filter examples labeled 'L' or 'R' for the second model
df_filtered_lr = df_filtered[(df_filtered['label'] == 'L') | (df_filtered['label'] == 'R')]

# Extract features and labels for L and R classification
X_lr = df_filtered_lr.iloc[:, 2:11].values
y_lr = df_filtered_lr['label'].values

# Standardize the data
X_lr = scaler.transform(X_lr)

# Encode labels for L and R classification
class_order_lr = ['L', 'R']
label_encoder_lr = LabelEncoder()
label_encoder_lr.fit(class_order_lr)
y_encoded_lr = label_encoder_lr.transform(df_filtered_lr['label'])

# Convert labels to categorical
y_one_hot_lr = to_categorical(y_encoded_lr)

# Split the data into train and test sets for L and R classification
X_train_lr, X_test_lr, y_train_lr, y_test_lr = train_test_split(
    X_lr, y_one_hot_lr, test_size=0.1, random_state=42
)

# Reshape the data for Conv1D layer
X_train_lr = X_train_lr.reshape(X_train_lr.shape[0], X_train_lr.shape[1], 1)
X_test_lr = X_test_lr.reshape(X_test_lr.shape[0], X_test_lr.shape[1], 1)

# Build the CNN model for L and R classification
model_lr = Sequential()
model_lr.add(Conv1D(filters=32, kernel_size=3, activation='relu', input_shape=(X_train_lr.shape[1], 1)))
model_lr.add(MaxPooling1D(pool_size=2))
model_lr.add(Flatten())
model_lr.add(Dense(10, activation='relu'))
model_lr.add(Dense(2, activation='softmax'))  # Output layer for L and R classification

# Compile the model
model_lr.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Train the model
model_lr.fit(X_train_lr, y_train_lr, epochs=10, batch_size=32, validation_split=0.2)

# Evaluate the model on the test set for L and R classification
accuracy_lr = model_lr.evaluate(X_test_lr, y_test_lr)[1]
print(f'L and R Classification Test Accuracy: {accuracy_lr}')