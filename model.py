import mne
import numpy as np
import pandas as pd
raw1 = mne.io.read_raw_brainvision('raw_data/AA0005.vhdr', preload=True)
raw2 = mne.io.read_raw_brainvision('raw_data/AA0007.vhdr', preload=True)

# Define a dictionary with your channel names and the type 'eeg'
channel_types = {name: 'eeg' for name in raw1.info['ch_names'] if 'acc' not in name}
raw1.set_channel_types(channel_types)
raw2.set_channel_types(channel_types)

#high pass filter to remove certain non hand motion related stuff
l_freq = 0.5
h_freq = 30  # to reduce noise, but can increase this to 40-60 if we are cutting off too much data
notch_freq = 60  # for powerlines

#exclude non-EEG channels
picks = mne.pick_types(raw1.info, meg=False, eeg=True, exclude='bads')

#filtering like we did in lab to remove noise
raw1.filter(l_freq=l_freq, h_freq=None, picks=picks)
raw2.filter(l_freq=l_freq, h_freq=None, picks=picks)

raw1.notch_filter(freqs=notch_freq)
raw2.notch_filter(freqs=notch_freq)


labels_df = pd.read_csv('eeg_labels_csv.csv')
label_timestamps = list(labels_df.itertuples(index=False, name=None))
#lets us pick the rate from the timestamps of how often examples happen
base_timestamp = 0
def label_data(raw, labels, rate=0.2):
    labeled_data = []
    current_label = 'N'
    label_idx = 0  # Index to keep track of which label we're on

    for start in np.arange(0, raw.times[-1], rate):
        end = start + rate
        # Update the label if necessary
        while label_idx < len(labels) and start >= labels[label_idx][0]:
            current_label = labels[label_idx][1]
            label_idx += 1

        start_sample = int(start * raw.info['sfreq'])
        end_sample = int(end * raw.info['sfreq'])
        end_sample = min(end_sample, len(raw.times))

        segment = raw.get_data(start=start_sample, stop=end_sample)
        avg_segment = segment.mean(axis=1)  # Average across time for each channel

        data_row = {'timestamp': start+base_timestamp, 'label': current_label}
        for i, ch_name in enumerate(raw.ch_names):
            data_row[ch_name] = avg_segment[i]

        labeled_data.append(data_row)

        if end_sample == len(raw.times):
            break

    return labeled_data


labeled_data_run1 = label_data(raw1, label_timestamps, rate=1)
base_timestamp=len(labeled_data_run1)
labeled_data_run2 = label_data(raw2, label_timestamps, rate=1)

# Combine data from both runs
combined_data = labeled_data_run1 + labeled_data_run2

# Create DataFrame
out_df = pd.DataFrame(combined_data)
out_df.set_index('timestamp', inplace=True)

print(out_df)
out_df.to_csv('labeled-data02.csv')
